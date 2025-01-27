package br.com.alura.forum.service

import br.com.alura.forum.dto.AtualizacaoTopicoDto
import br.com.alura.forum.dto.NovoTopicoDto
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.exception.NotFoundException
import br.com.alura.forum.mapper.TopicoFormMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.model.Curso
import br.com.alura.forum.model.Topico
import br.com.alura.forum.model.Usuario
import br.com.alura.forum.repository.TopicoRepository
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors
import kotlin.apply

@Service
class TopicoService(
    private val repository: TopicoRepository,
    private val topicoViewMapper: TopicoViewMapper,
    private val topicoFormMapper: TopicoFormMapper,
    private val notFoundMessage: String = "Tópico não encontrado"
    ) {

    fun listar(): List<TopicoView> {
        return repository.findAll().map { t -> topicoViewMapper.map(t) }
    }

    fun buscarPorId(id: Long): TopicoView {
        val topico = repository.findById(id)
            .orElseThrow{NotFoundException(notFoundMessage)}

        return topicoViewMapper.map(topico)
    }

    fun cadastrar(dto: NovoTopicoDto) : TopicoView{
        val topico = topicoFormMapper.map(dto)
        repository.save(topico)

        return topicoViewMapper.map(topico)
    }

    fun atualizar(dto: AtualizacaoTopicoDto): TopicoView? {
        val topico = repository.findById(dto.id)
            .orElseThrow{NotFoundException(notFoundMessage)}

        topico?.let {
            topico.titulo = dto.titulo
            topico.mensagem = dto.mensagem
            return topicoViewMapper.map(topico)
        }

        return null
    }

    fun deletar(id: Long) {
        repository.deleteById(id)
    }
}