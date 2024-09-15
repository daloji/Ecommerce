package com.ecommerce.cozashop.model;

import static java.util.Objects.nonNull;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.joda.time.LocalDateTime;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

public class PdfTemplate {

	private static final String IMAGE_LOGO = "static/images/icons/logo-01.png";
	
	ResourceLoader resourceLoader;
	
	public PdfTemplate(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	public Map<String,String> generateInvoice(Map<ProductItem,Integer> mapProductQty,User user,Address adresse) {
		float threecol = 190f;
		float twocol = 230f;
		float twocol150 = twocol + 150f;
		float twocolumnWidth[] = {twocol150 , twocol};
		float fullwidth[] = {threecol * 3};
		float threeColumnWidth[] = {threecol , threecol , threecol};
		Map<String,String> mapFile = new HashMap<>();
		try {
			String filename = generateFileName(user);
			Path temp = Files.createTempFile(filename, ".pdf");
			PdfWriter pdfWriter = new PdfWriter(temp.toString());
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			pdfDocument.setDefaultPageSize(PageSize.A4);
			Document document = new Document(pdfDocument);
			Image imageData = createLogo();
			if(nonNull(imageData)) {
				Paragraph onesp = new Paragraph("\n\n");
				document.add(imageData);
				Paragraph sp = new Paragraph("\n");
				document.add(sp);
				String invoiceNumber = generateInvoiceNumber();
				mapFile.put(invoiceNumber, temp.toString());
				LocalDateTime currentDateTime =  LocalDateTime.now();
				String date = currentDateTime.toString("dd-MM-yyyy");
				Table table = new Table(twocolumnWidth);
				table.addCell(new Cell().add("").setBorder(Border.NO_BORDER).setBold());
				Table nestedTable = new Table(new float[]{twocol/2 , twocol/2});
				nestedTable.addCell(getHeaderTextCell("Facture No :"));
				Cell cell4 = new Cell().add(invoiceNumber).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				nestedTable.addCell(cell4);
				nestedTable.addCell(getHeaderTextCell("Invoice Date :"));
				Cell cell6 = new Cell().add(date).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				nestedTable.addCell(cell6);
	
				table.addCell(new Cell().add(nestedTable).setBorder(Border.NO_BORDER));
				Border border = new SolidBorder(Color.GRAY,2f);
				Table divider = new Table(fullwidth);
				divider.setBorder(border);
				document.add(table);
				document.add(sp);
				
				
				Table tables = new Table(twocolumnWidth);
				Table adresseTable = new Table(new float[]{5f/2 });
				Cell cell =  new Cell().add("DA Logiciel").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell);
				Cell cell1 = new Cell().add("9 rue Nathalie lemel").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell1);
				Cell cell2 = new Cell().add("35000 rennes").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell2);
				cell2 = new Cell().add("France").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell2);
				tables.addCell(new Cell().add(adresseTable).setBorder(Border.NO_BORDER));
				
				 adresseTable = new Table(new float[]{5f/2 });
				 cell =  new Cell().add("DA Logiciel").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell);
				 cell1 = new Cell().add("9 rue Nathalie lemel").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell1);
				 cell2 = new Cell().add("35000 rennes").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell2);
				cell2 = new Cell().add("France").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell2);
				tables.addCell(new Cell().add(adresseTable).setBorder(Border.NO_BORDER));
				document.add(tables);
				document.add(sp);
				document.add(divider);
				
				float twocolumnWidth2[] = {twocol150 , 20f};

				Table twoColTable = new Table(twocolumnWidth2);
				cell = new Cell().add("Billing Information").setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				twoColTable.addCell(cell);
				cell = new Cell().add("Shipping Information").setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);

				twoColTable.addCell(cell);

				document.add(twoColTable);

				createBillingInformation(document, user, adresse);
				document.add(sp);
				document.add(divider);
				document.add(sp);

				Paragraph productParagraph = new Paragraph("Products");
				document.add(productParagraph.setBold());

				Table threeColumnTable1 = new Table(threeColumnWidth);
				threeColumnTable1.setBackgroundColor(Color.BLACK , 0.7f);
				threeColumnTable1.addCell(new Cell().add("Description").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
				threeColumnTable1.addCell(new Cell().add("Quantity").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
				threeColumnTable1.addCell(new Cell().add("Price").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
				document.add(threeColumnTable1);

				Table threeColTable2 = new Table(threeColumnWidth);
				double totalSum=0f;
				if(!mapProductQty.isEmpty()) {
					for (Map.Entry<ProductItem,Integer> entry : mapProductQty.entrySet()) {
						ProductItem productItem = entry.getKey();
						Integer qty = entry.getValue();
						double total = qty*productItem.getPrice();
						totalSum += total;
						threeColTable2.addCell(new Cell().add(productItem.getProduct_id().getProduct_name()).setBorder(Border.NO_BORDER)).setMarginLeft(10f);
						threeColTable2.addCell(new Cell().add(String.valueOf(qty)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
						threeColTable2.addCell(new Cell().add(String.valueOf(total)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
					}
				}

				document.add(threeColTable2.setMarginBottom(20f));

				Table tableDivider2 = new Table(fullwidth);
				float totalDashedLine[] = {threecol+125f , threecol*2};
				Table threeColTable4 = new Table(totalDashedLine);
				threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
				threeColTable4.addCell(new Cell().add(tableDivider2).setBorder(Border.NO_BORDER));
				document.add(threeColTable4);

				Table threeColTable3 = new Table(threeColumnWidth);
				threeColTable3.setBackgroundColor(Color.BLACK , 0.7f);
				threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER)).setMarginLeft(10f);
				threeColTable3.addCell(new Cell().add("Total").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
				threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)).setMarginRight(15f).setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER);

				document.add(threeColTable3);
				document.add(tableDivider2);
				document.add(new Paragraph("\n"));
				document.add(divider.setBorder(new SolidBorder(Color.GRAY , 1)).setMarginBottom(15f));
				document.close();
				
				
				
			}
			
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapFile;
		/*
		try {
			String filename = generateFileName(user);
			Path temp = Files.createTempFile(filename, ".pdf");
			PdfWriter pdfWriter = new PdfWriter(temp.toString());
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			pdfDocument.setDefaultPageSize(PageSize.A4);
			Document document = new Document(pdfDocument);
			Image imageData = createLogo();
			if(nonNull(imageData)) {
				Paragraph onesp = new Paragraph("\n\n\n\n");
				document.add(imageData);
				document.add(onesp);
			}

			float threecol = 190f;
			float twocol = 230f;
			float twocol150 = twocol + 150f;
			float twocolumnWidth[] = {twocol150 , 10f};
			float fullwidth[] = {threecol * 3};
			float threeColumnWidth[] = {threecol , threecol , threecol};
			Table table = new Table(twocolumnWidth);


			/////
			Table adresseTable = new Table(new float[]{5f/2 });
			Cell cell =  new Cell().add("DA Logiciel").setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
			adresseTable.addCell(cell);
			Cell cell1 = new Cell().add("9 rue Nathalie lemel").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
			adresseTable.addCell(cell1);
			Cell cell2 = new Cell().add("35000 rennes").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
			adresseTable.addCell(cell2);
			cell2 = new Cell().add("France").setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
			adresseTable.addCell(cell2);
			table.addCell(new Cell().add(adresseTable).setBorder(Border.NO_BORDER));		

			Table tableAdresse = createAddresseCustomer(user,adresse);
			table.addCell(new Cell().add(tableAdresse).setBorder(Border.NO_BORDER));

			Border border = new SolidBorder(Color.GRAY,2f);
			Table divider = new Table(fullwidth);
			divider.setBorder(border);

			document.add(table);
			divider.setBorder(border);

			Paragraph divsp = new Paragraph("\n");
			document.add(divsp);
			document.add(divider);
			document.add(divsp);

			float twocolumnWidth2[] = {twocol150 , 20f};

			Table twoColTable = new Table(twocolumnWidth2);
			cell = new Cell().add("Billing Information").setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
			twoColTable.addCell(cell);
			cell = new Cell().add("Shipping Information").setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);

			twoColTable.addCell(cell);

			document.add(twoColTable);

			createBillingInformation(document, user, adresse);
			document.add(divsp);
			document.add(divider);
			document.add(divsp);

			Paragraph productParagraph = new Paragraph("Products");
			document.add(productParagraph.setBold());

			Table threeColumnTable1 = new Table(threeColumnWidth);
			threeColumnTable1.setBackgroundColor(Color.BLACK , 0.7f);
			threeColumnTable1.addCell(new Cell().add("Description").setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
			threeColumnTable1.addCell(new Cell().add("Quantity").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
			threeColumnTable1.addCell(new Cell().add("Price").setBold().setFontColor(Color.WHITE).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
			document.add(threeColumnTable1);

			Table threeColTable2 = new Table(threeColumnWidth);
			double totalSum=0f;
			if(!mapProductQty.isEmpty()) {
				for (Map.Entry<ProductItem,Integer> entry : mapProductQty.entrySet()) {
					ProductItem productItem = entry.getKey();
					Integer qty = entry.getValue();
					double total = qty*productItem.getPrice();
					totalSum += total;
					threeColTable2.addCell(new Cell().add(productItem.getProduct_id().getProduct_name()).setBorder(Border.NO_BORDER)).setMarginLeft(10f);
					threeColTable2.addCell(new Cell().add(String.valueOf(qty)).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
					threeColTable2.addCell(new Cell().add(String.valueOf(total)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER).setMarginRight(15f));
				}
			}

			document.add(threeColTable2.setMarginBottom(20f));

			Table tableDivider2 = new Table(fullwidth);
			float totalDashedLine[] = {threecol+125f , threecol*2};
			Table threeColTable4 = new Table(totalDashedLine);
			threeColTable4.addCell(new Cell().add("").setBorder(Border.NO_BORDER));
			threeColTable4.addCell(new Cell().add(tableDivider2).setBorder(Border.NO_BORDER));
			document.add(threeColTable4);

			Table threeColTable3 = new Table(threeColumnWidth);
			threeColTable3.setBackgroundColor(Color.BLACK , 0.7f);
			threeColTable3.addCell(new Cell().add("").setBorder(Border.NO_BORDER)).setMarginLeft(10f);
			threeColTable3.addCell(new Cell().add("Total").setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER).setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER));
			threeColTable3.addCell(new Cell().add(String.valueOf(totalSum)).setTextAlignment(TextAlignment.RIGHT).setBorder(Border.NO_BORDER)).setMarginRight(15f).setBold().setFontColor(Color.WHITE).setBorder(Border.NO_BORDER);

			document.add(threeColTable3);
			document.add(tableDivider2);
			document.add(new Paragraph("\n"));
			document.add(divider.setBorder(new SolidBorder(Color.GRAY , 1)).setMarginBottom(15f));
			document.close();
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}

	
	private String generateInvoiceNumber() {
		Random rand = new Random();
		int random = rand.nextInt(1000);
		LocalDateTime currentDateTime =  LocalDateTime.now();
		String invoiceName = currentDateTime.toString("yyyy-MM")+"-"+random;
		return invoiceName;
	}
	private Image createLogo() throws URISyntaxException, IOException {
		Resource resource = resourceLoader.getResource("classpath:"+IMAGE_LOGO);
		if(nonNull(resource)) {
			File file = resource.getFile();
			ImageData imageDataLogo = ImageDataFactory.create(file.toString());
			Image imagelogo = new Image(imageDataLogo);
			imagelogo.setHorizontalAlignment(HorizontalAlignment.LEFT);
			return imagelogo;
		}
		return null;
	}

	private String generateFileName(User user) {
		String filename = "";
		LocalDateTime currentDateTime =  LocalDateTime.now();
		filename = user.getFirst_name()+'_'+user.getLast_name()+"_"+currentDateTime.toString("MM_dd_yyyy_hh_mm");;
		return filename;
	}



	private void createBillingInformation(Document document,User user,Address adresse) {
		float twocol = 230f;
		float twocol150 = twocol + 150f;
		float twocolumnWidth[] = {twocol150 , 10f};
		Table twoColTable2 = new Table(twocolumnWidth);
		Cell myCell = new Cell().add("Client Id").setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
		twoColTable2.addCell(myCell.setBold());
		myCell = new Cell().add("Name").setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
		twoColTable2.addCell(myCell.setBold());
		myCell = new Cell().add(Long.toString(user.getId())).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
		twoColTable2.addCell(myCell);
		myCell = new Cell().add(user.getFirst_name()+" "+user.getLast_name()).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
		twoColTable2.addCell(myCell);

		document.add(twoColTable2);

		Table twoColTable3 = new Table(twocolumnWidth);
		myCell = new Cell().add("email").setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
		twoColTable3.addCell(myCell.setBold());
		myCell = new Cell().add("Address").setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
		twoColTable3.addCell(myCell.setBold());
		myCell = new Cell().add(user.getEmail()).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
		twoColTable3.addCell(myCell);

		myCell = new Cell().add(buildAdresse(adresse)).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
		twoColTable3.addCell(myCell);

		document.add(twoColTable3);

	}

	private String buildAdresse(Address adresse) {
		String addresse = "";
		if(nonNull(adresse)) {

			if(nonNull(adresse.getComplement())) {
				addresse = adresse.getComplement() +"\n";
			}
			if(nonNull(adresse.getRoad())) {
				addresse = addresse + adresse.getRoad() + "\n";
			}
			if(nonNull(adresse.getDistrict())) {
				addresse = addresse + adresse.getDistrict();
			}
			if(nonNull(adresse.getCity())) {
				addresse = addresse +" "+adresse.getCity() +"\n";
			}
			if(nonNull(adresse.getCountry())) {
				addresse = addresse + adresse.getCountry();
			}
		}
		return addresse;
	}


	private Table createAddresseCustomer(User user,Address adresse) {
		Table adresseTable = new Table(new float[]{5f/2 });
		if(nonNull(adresse)) {
			Cell cell =  new Cell().add(user.getFirst_name() +" " + user.getLast_name()).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
			adresseTable.addCell(cell);
			String addresse ="";
			addresse = adresse.getRoad();
			if(nonNull(adresse.getComplement())) {
				addresse = addresse + " "+ adresse.getComplement();
				Cell cell1 = new Cell().add(addresse).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell1);
			}

			if(nonNull(adresse.getDistrict())) {
				addresse = adresse.getDistrict() + " "+ adresse.getCountry();
				Cell cell2 = new Cell().add(addresse).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell2);
			}

			if(nonNull(adresse.getCountry())) {
				Cell cell2 = new Cell().add(adresse.getCountry()).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
				adresseTable.addCell(cell2);	
			}


		}

		return adresseTable;
	}
	
	static Cell getHeaderTextCell(String textValue) {
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getHeaderTextCellValue(String textValue) {
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getBillingandShippingCell(String textValue) {
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell10fLeft(String textValue , Boolean isBold) {
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ?myCell.setBold():myCell;
    }
}
