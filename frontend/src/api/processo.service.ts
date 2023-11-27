import { API } from "../api/Api";

export interface IProcesso {
  id: number;
  numProcesso: string;
  status: string;
  dataAtualizacao: string;
  descricao: string;
  nomeReu: string[];
  nomeAutor: string[];
}

export interface IManualProcesso {
  advogado: object,


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

  //export async function cadastrarProcessoManual()
