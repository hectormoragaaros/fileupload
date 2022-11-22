package org.hectormoraga.fileuploaddemo.repository;

import java.util.UUID;

import org.hectormoraga.fileuploaddemo.model.FileDB;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileDBRepository extends JpaRepository<FileDB, UUID>{

}
