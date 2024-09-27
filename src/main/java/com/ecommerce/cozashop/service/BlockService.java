package com.ecommerce.cozashop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.Block;
import com.ecommerce.cozashop.repository.BlockRepo;

@Service
public class BlockService {

	@Autowired
	private BlockRepo blockRepo;


	public void addBlock(Block block) {
		blockRepo.save(block);
	}

	
	public void deleteBlock(Integer id) {
		blockRepo.deleteById(id);
		
	}
	public List<Block> findAllBlock(){
		List<Block> listBlock = blockRepo.findAll();
		return listBlock;
	}
	
	public Block findBlockById(int id) {
		Block block = null;
		Optional<Block> optBlock =  blockRepo.findById(id);	
		if(optBlock.isPresent()) {
			block = optBlock.get();
		}
		return block;
	}
}
