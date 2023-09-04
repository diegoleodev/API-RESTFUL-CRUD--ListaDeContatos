package com.example.contacts.repositories;

import com.example.contacts.models.ContactModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ContactsRepository extends JpaRepository<ContactModel, UUID> {
}
