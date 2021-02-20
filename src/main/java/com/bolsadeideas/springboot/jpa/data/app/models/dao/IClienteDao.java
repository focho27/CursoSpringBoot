package com.bolsadeideas.springboot.jpa.data.app.models.dao;

//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.bolsadeideas.springboot.jpa.data.app.models.entity.Cliente;



public interface IClienteDao extends PagingAndSortingRepository<Cliente,Long>{
		
	
}
