package com.kema.management.repository;

import com.kema.management.entities.NodeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeInfoRepository extends JpaRepository<NodeInfo, Long> {
}
