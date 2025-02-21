package com.metazdecimal.employee.repository;

import com.metazdecimal.employee.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
