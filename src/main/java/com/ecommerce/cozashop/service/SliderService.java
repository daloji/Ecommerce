package com.ecommerce.cozashop.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.cozashop.model.Slider;
import com.ecommerce.cozashop.repository.SliderRepo;
import static java.util.Objects.nonNull;

@Service
public class SliderService {

    @Autowired
    private SliderRepo sliderRepo;
    
    
    public Slider findAllSlider() {
    	Slider slider = null;
    	List<Slider>  listSlider =  sliderRepo.findAll();
    	if(nonNull(listSlider) && !listSlider.isEmpty()) {
    		slider = listSlider.get(0);
    	}
    	return slider;
    }
    
    public void save(Slider slider) {
    	sliderRepo.save(slider);
    }
    
    public void delete(Integer id) {
    	sliderRepo.deleteById(id);
    }
    
    public Slider findSliderById(int id) {
    	Slider slide = null;
    	Optional<Slider> optSlider = sliderRepo.findById(id);
    	if(optSlider.isPresent()) {
    		slide = optSlider.get();
    	}
    	return slide;	
    }
}
