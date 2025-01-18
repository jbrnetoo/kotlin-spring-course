package br.com.alura.forum.service

import br.com.alura.forum.dto.AtualizacaoTopicoDto
import br.com.alura.forum.dto.NovoTopicoDto
import br.com.alura.forum.dto.TopicoView
import br.com.alura.forum.mapper.TopicoFormMapper
import br.com.alura.forum.mapper.TopicoViewMapper
import br.com.alura.forum.model.Curso
import br.com.alura.forum.model.Topico
import br.com.alura.forum.model.Usuario
import org.springframework.stereotype.Service
import java.util.*
import java.util.stream.Collectors

@Service
class TopicoService(
    private var topicos: List<Topico>,
    private val topicoViewMapper: TopicoViewMapper,
    private val topicoFormMapper: TopicoFormMapper
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
        val topico = topicos.first()

        return topicoViewMapper.map(topico)
    }

    fun cadastrar(dto: NovoTopicoDto) {
        val topico = topicoFormMapper.map(dto)
        topico.id = topicos.size.toLong() + 1

        topicos = topicos.plus(topico)
    }

    fun atualizar(dto: AtualizacaoTopicoDto) {
        val topico = topicos.find { it.id == dto.id  }

        topico?.let {
            topicos = topicos.minus(it).plus(Topico(
                id = dto.id,
                titulo = dto.titulo,
                mensagem = dto.mensagem,
                autor = it.autor,
                curso = it.curso,
                respostas =  it.respostas,
                status = it.status,
                dataCriacao = it.dataCriacao
            ))
        }
    }

    fun deletar(id: Long) {
        val topico = topicos.first { it.id == id  }
        topicos = topicos.minus(topico)
    }
}