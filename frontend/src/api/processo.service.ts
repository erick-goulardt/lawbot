import { API } from "../api/Api";
import { Autor, IDefineCliente, IDefineDescricao, Reu } from "../types/Types";

export async function retornaProcessos(id: number) {
  try {
    const response = await API.get(`/processo/advogado/${id}`);
    return response;
  } catch (error) {
    console.error(error);
    return null;
  }
}
export async function cadastrarProcessosEmBloco(file: File, id: number) {
    try {
      const formData = new FormData();
      formData.append('file', file);
      const response = await API.post(`/processo/cadastro-arquivo/${id}`, formData, {headers: {
        'Content-Type': 'multipart/form-data',
      },});
  
      return response.data;
    } catch (error) {
      console.error('Erro ao cadastrar processos em bloco:', error);
      return null;
    }
  }

  export async function definirCliente(data : IDefineCliente) {
    try {
      const response = await API.put(`/processo/definir-cliente`, data);
      return response;
    } catch (error) {
      console.error('Erro ao definir cliente:', error);
      return null;
    }
  }

  export async function getProcessoComCliente(id: number) {
    try {
      const response = await API.get(`/processo/comCliente/${id}`);
      return response;
    } catch (error) {
      console.error('Erro ao definir cliente:', error);
      return null;
    }
  }

  export async function getProcessoSemCliente(id: number) {
    try {
      const response = await API.get(`/processo/semCliente/${id}`);
      return response;
    } catch (error) {
      console.error('Erro ao definir cliente:', error);
      return null;
    }
  }

  export async function setDescricaoProc(data : IDefineDescricao) {
    try {
      const response = await API.put(`/processo/definir-descricao`, data);
      return response;
    } catch (error) {
      console.error('Erro ao descricao:', error);
      return null;
    }
  }

  export async function obterHistoricoDoProcesso(id: number) {
    try {
      const response = await API.get(`/processo/historico/${id}`);
      return response;
    } catch (error) {
      console.error('Erro ao descricao:', error);
      return null;
    }
  }

  export async function registrarProcessoManual(idAdvogado: number | undefined, idCliente: number,
    ultimoEvento: string, dataAtualizacao: string, descricao: string, numeroProcesso: string,
    classe: string, localidade: string, assunto: string, nomeReu: Reu, nomeAutor: Autor) {
    try {
      await API.post(`/processo/cadastro`), 
      { idAdvogado, idCliente, ultimoEvento, dataAtualizacao, descricao, numeroProcesso, classe, localidade, assunto, nomeReu, nomeAutor }
      console.log("Cadastrou!");
    } catch (error) {
      console.error("Erro no cadastro!");
    }
  }
