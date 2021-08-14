package com.cwi.cooperative.voting.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Entidade que representa a Sessão de votação
 * @author ricardo.spinoza
 *
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PollingStation {	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(optional = false)
	private Topic topic;
	@Column(nullable = false, updatable = false)
	private LocalDateTime startPeriod;
	@Column(updatable = false)
	private LocalDateTime closePeriod;
	@OneToMany
	private List<Vote> voteList;
}