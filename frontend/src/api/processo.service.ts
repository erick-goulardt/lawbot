import { API } from "../api/Api";

interface Reu {
  nome: string;
}

interface Autor {
  nome: string;
}

export interface IProcesso {
  id: number;
  numProcesso: string;
  status: string;
  classe: string;
  assunto: string;
  dataAtualizacao: string;
  descricao: string;
  nomeReu: Reu[];
  nomeAutor: Autor[];
}

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
