package com.example.contacts.controllers;

import com.example.contacts.dtos.ContactRecordDto;
import com.example.contacts.models.ContactModel;
import com.example.contacts.repositories.ContactsRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ContactController {

    @Autowired
    ContactsRepository contactsRepository;

    @PostMapping("/contacts")
    public ResponseEntity<ContactModel> saveContact(@RequestBody @Valid ContactRecordDto contactRecordDto){
        var contactModel = new ContactModel();
        BeanUtils.copyProperties(contactRecordDto, contactModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactsRepository.save(contactModel));
    }

    @GetMapping("/contacts")
    public ResponseEntity<List<ContactModel>> getAllContacts(){
        List<ContactModel> contactsList = contactsRepository.findAll();
        if (!contactsList.isEmpty()){
            for (ContactModel contact : contactsList){
                UUID id = contact.getIdContact();
                contact.add(linkTo(methodOn(ContactController.class).getOneContact(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(contactsList);
    }
    @GetMapping("/contacts/{id}")
    public ResponseEntity<Object>  getOneContact(@PathVariable(value = "id") UUID id){
        Optional<ContactModel> contactO = contactsRepository.findById(id);
        if (contactO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato não existente!");
        }
        contactO.get().add(linkTo(methodOn(ContactController.class).getAllContacts()).withRel("Lista de Contatos"));
        return ResponseEntity.status(HttpStatus.OK).body(contactO.get());
    }

    @PutMapping("/contacts/{id}")
    public ResponseEntity<Object> updateContact(@PathVariable(value = "id")UUID id , @RequestBody @Valid ContactRecordDto contactRecordDto) {
        Optional<ContactModel> contactO  = contactsRepository.findById(id);
        if (contactO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato não existente!");
        }
        var contactModel = contactO.get();
        BeanUtils.copyProperties(contactRecordDto, contactModel);
        return ResponseEntity.status(HttpStatus.OK).body(contactsRepository.save(contactModel));
    }

    @DeleteMapping("/contacts/{id}")
    public ResponseEntity<Object> deleteContact(@PathVariable(value = "id")UUID id) {
        Optional<ContactModel> contactO  = contactsRepository.findById(id);
        if (contactO.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Contato não existente!");
        }
        contactsRepository.delete(contactO.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto deletado com sucesso!!!");
    }
}
