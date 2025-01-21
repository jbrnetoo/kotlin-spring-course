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
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors
import kotlin.apply

@Service
class TopicoService(
    private var topicos: List<Topico>,
    private val topicoViewMapper: TopicoViewMapper,
    private val topicoFormMapper: TopicoFormMapper,
    private val notFoundMessage: String = "Tópico não encontrado"
    ) {

    init {
        val topico = Topico(
                id = 1,
                titulo = "Duvida Kotlin",
                mensagem = "Variaveis no Kotlin",
                curso = Curso(
                        id = 1,
                        nome = "Kotlin",
                        categoria = "Programacao"
                ),
                autor = Usuario(
                        id = 1,
                        nome = "Ana da Silva",
                        email = "ana@email.com"
                )
        )

        topicos = Arrays.asList(topico)
    }

    fun listar(): List<TopicoView> {
        return topicos.map { t -> topicoViewMapper.map(t) }
    }

    fun buscarPorId(id: Long): TopicoView {
        val topico = topicos.firstOrNull()

        if(topico == null){
            throw NotFoundException(notFoundMessage)
        }

        return topicoViewMapper.map(topico)
    }

    fun cadastrar(dto: NovoTopicoDto) : TopicoView{
        val topico = topicoFormMapper.map(dto)
        topico.id = topicos.size.toLong() + 1
        topicos = topicos.plus(topico)
        return topicoViewMapper.map(topico)
    }

    fun atualizar(dto: AtualizacaoTopicoDto): TopicoView? {
        val topico = topicos.find { it.id == dto.id  }

        topico?.let {
            val topicoAtualizado = Topico(
                id = dto.id,
                titulo = dto.titulo,
                mensagem = dto.mensagem,
                autor = it.autor,
                curso = it.curso,
                respostas =  it.respostas,
                status = it.status,
                dataCriacao = it.dataCriacao
            )

            topicos = topicos.minus(it).plus(topicoAtualizado)
            return topicoViewMapper.map(topicoAtualizado)
        }

        return null
    }

    fun deletar(id: Long) {
        val topico = topicos.firstOrNull { it.id == id  }

        if(topico == null){
            throw NotFoundException(notFoundMessage)
        }
        topicos = topicos.minus(topico)
    }
}