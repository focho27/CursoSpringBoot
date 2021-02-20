package com.bolsadeideas.springboot.jpa.data.app.controllers;


import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

//import com.bolsadeideas.springboot.jpa.data.app.models.dao.IClienteDao;
import com.bolsadeideas.springboot.jpa.data.app.models.entity.Cliente;
import com.bolsadeideas.springboot.jpa.data.app.models.service.IClienteService;
import com.bolsadeideas.springboot.jpa.data.app.util.paginator.PageRender;


@Controller
@SessionAttributes("cliente")
public class ClientesController {
	
	@Autowired
	@Qualifier("ClienteService")
	private IClienteService clienteService;
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	// .+ para que agregue la extension del archivo
	@GetMapping(value="/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename){
		
		//resolve concatena otro path con el /
		Path pathFoto = Paths.get("uploads").resolve(filename).toAbsolutePath();
		log.info("PathFoto " + pathFoto);
		Resource recurso = null;
		try {
			recurso = new UrlResource(pathFoto.toUri());
			if((!recurso.exists()) || (!recurso.isReadable())) {
				
				throw new RuntimeException("Error no se puede cargar la imagen: " + pathFoto.toString());
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""+recurso.getFilename()+"\"").body(recurso);
		
	}
	
	
	@GetMapping(value="/ver/{id}")
	public String ver(@PathVariable(value="id") Long id, Map<String,Object> model, RedirectAttributes flash) {
		Cliente cliente = clienteService.findOne(id);
		if(cliente == null) {
			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");
			return "redirect:/listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Detalle cliente: " + cliente.getNombre());
		return "ver";
		
	}
	
	@RequestMapping(value="/listar",method=RequestMethod.GET)
	public String listar(@RequestParam(name="page",defaultValue="0") int page,Model model) {
		//page = (page == null) ? 1 : page;
		Pageable pageRequest = PageRequest.of(page,3);
		// Para paginar 
		Page<Cliente> clientes = clienteService.findAll(pageRequest);
		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);
		
		model.addAttribute("titulo","Listado de clientes");
		model.addAttribute("clientes",clientes);
		model.addAttribute("page",pageRender);
		
		return "listar";
		
	}
	
	@RequestMapping(value="/form",method=RequestMethod.GET)
	public String crear( Map<String,Object> model) {
		Cliente cliente = new Cliente();
		
	
		model.put("titulo", "Formulario de cliente");
		model.put("cliente", cliente);
		return "form";
	}
	
	
	@RequestMapping(value="/form/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String,Object> model,RedirectAttributes flash) {
		
		Cliente cliente = null;
		
		if(id>0) {
			cliente = clienteService.findOne(id);
			if(cliente==null) {
				flash.addFlashAttribute("error","El ID de cliente no existe en la Base de Datos");
				return "redirect:listar";
			}
		}else {
			flash.addFlashAttribute("error","El ID del cliente no puede ser '0'");
			return "redirect:listar";
		}
		model.put("cliente", cliente);
		model.put("titulo", "Editar cliente");
		return "form";
	}
	
	@RequestMapping(value="/form",method=RequestMethod.POST)
	 public String guardar(@Valid Cliente cliente,BindingResult result,Model model,@RequestParam(name="file")MultipartFile foto,RedirectAttributes flash, SessionStatus status) {
		 
		 if(result.hasErrors()) {
			 model.addAttribute("titulo","Formulario de cliente");
			 return "form";
			
		 }
		 if(!foto.isEmpty()) {
			 /*Path directorioRecursos = Paths.get("src//main//resources//static/upload");
			 String rootPath = directorioRecursos.toFile().getAbsolutePath();*/
			 String uniqueFileName = UUID.randomUUID().toString() + "_" + foto.getOriginalFilename();
			 Path rootPath = Paths.get("uploads").resolve(uniqueFileName);
			 Path rootAbsolutePath= rootPath.toAbsolutePath();
			 log.info("RootPath: " + rootPath);
			 
			 log.info("RootAbsolute " + rootAbsolutePath);
			 
			 log.info(uniqueFileName);
			 try {
				/*byte[] bytes = foto.getBytes();
				
				Path rutaCompleta = Paths.get(rootPath+"//"+foto.getOriginalFilename());
				Files.write(rutaCompleta, bytes);*/
				Files.copy(foto.getInputStream(),rootAbsolutePath);
				flash.addFlashAttribute("info","Ha subido correctamente la foto '" + uniqueFileName+ "' ");
				cliente.setFoto(uniqueFileName);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		 }
		 String mensajeFlash= (cliente.getId()!=null) ? "Cliente editado con éxito" : "Cliente creado con éxito";
		clienteService.save(cliente);
		status.setComplete();
		flash.addFlashAttribute("success",mensajeFlash);
		return "redirect:/listar";
		 
	 }
	 
	 @RequestMapping(value="/eliminar/{id}")
	 public String eliminar(@PathVariable(value="id") Long id ,RedirectAttributes flash) {

			if(id>0 && id!=null) {
				clienteService.delete(id);
				flash.addFlashAttribute("success","Cliente eliminado con éxito");
			}
		 
			
		 return "redirect:/listar";
	 }
	 
	 
	 
	 
}
