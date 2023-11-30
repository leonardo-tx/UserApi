package com.at.userapi.service;

import com.at.userapi.dto.UsuarioDTOInput;
import com.at.userapi.dto.UsuarioDTOOutput;
import com.at.userapi.model.Usuario;
import org.modelmapper.MappingException;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public final class UsuarioService {
    private final List<Usuario> listaUsuarios = new ArrayList<>();
    private final ModelMapper modelMapper = new ModelMapper();

    public List<UsuarioDTOOutput> listar() {
        ArrayList<UsuarioDTOOutput> output = new ArrayList<>(listaUsuarios.size());

        for (Usuario usuario : listaUsuarios) {
            UsuarioDTOOutput usuarioOutput = modelMapper.map(usuario, UsuarioDTOOutput.class);
            output.add(usuarioOutput);
        }
        return output;
    }

    public boolean inserir(UsuarioDTOInput usuarioInput) throws MappingException {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getId() == usuarioInput.getId()) return false;
        }
        Usuario usuario = modelMapper.map(usuarioInput, Usuario.class);
        listaUsuarios.add(usuario);

        return true;
    }

    public boolean alterar(UsuarioDTOInput usuarioInput) throws MappingException {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario currentUsuario = listaUsuarios.get(i);
            if (currentUsuario.getId() != usuarioInput.getId()) continue;

            Usuario usuario = modelMapper.map(usuarioInput, Usuario.class);
            listaUsuarios.set(i, usuario);

            return true;
        }
        return false;
    }

    public UsuarioDTOOutput buscar(int id) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getId() != id) continue;
            return modelMapper.map(usuario, UsuarioDTOOutput.class);
        }
        return null;
    }

    public boolean excluir(int id) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            if (listaUsuarios.get(i).getId() != id) continue;

            listaUsuarios.remove(i);
            return true;
        }
        return false;
    }
}