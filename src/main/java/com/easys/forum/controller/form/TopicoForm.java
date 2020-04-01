package com.easys.forum.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.easys.forum.model.Curso;
import com.easys.forum.model.Topico;
import com.easys.forum.repository.CursoRepository;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopicoForm {

	@NotNull
	@NotEmpty
	@Length(min = 5)
	private String titulo;

	@NotNull
	@NotEmpty
	@Length(min = 10)
	private String mensagem;

	@NotNull
	@NotEmpty
	private String nomeCurso;

	public Topico converter(CursoRepository cursoRepository) {
		Curso curso = cursoRepository.findByNome(nomeCurso);
		return Topico.builder().titulo(titulo).mensagem(mensagem).curso(curso).build();
	}
}
