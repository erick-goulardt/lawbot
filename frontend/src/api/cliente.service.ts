import { IEditCliente } from "../types/Types";
import { API } from "./Api";

export async function registerCliente(idAdvogado: number | undefined, nome: string, email: string, senha: string, cpf: string, dataNascimento: string) {
    try {
      const data = await API.post(
        `/cliente/cadastro`,
        {idAdvogado, nome, email, senha, cpf, dataNascimento }
      );
      return data;
    } catch (error) {
      console.error(error);
      return null;
    }
  }


export async function deleteCliente(id: number) {
  try {
    await API.delete(`/cliente/${id}`)
  } catch (error) {
    console.error(error);
  }
}

export async function editCliente(clienteId: number, request: IEditCliente) {
  try {
    await API.put(`/cliente/editar/${clienteId}`, request)
  } catch (error) {
    console.error(error);
  }
}