package com.cwi.cooperative.voting.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entidade que representa a pauta de votação
 * @author ricardo.spinoza
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Topic {	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique=true ,nullable = false)
	private String title;
	@Column(nullable = true)
	private String description;	
}