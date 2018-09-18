package com.kema.simulator.ssh.repository;

import com.kema.simulator.ssh.common.SshDef;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SshDefRepository extends JpaRepository<SshDef, Long> {

}
