import { IProfile, IEditProfile } from "../types/Types";
import { API } from "./Api";

export async function getProfile(id: number): Promise<IProfile | null> {
    try {
        const response = await API.get(`/advogado/info/${id}`);
        const data: IProfile = response.data; 
        return data;
      } catch (error) {
        console.error(error);
        return null
      }
    }

export async function deleteProfile(id: number) {
  try {
    await API.delete(`/advogado/deletar/${id}`)
    console.log("Deletado!")
  } catch (error) {
    console.error(error)
    console.log("Não deletado!")
  }
}

export async function updateProfile(id: number, request: IEditProfile) {
  try {
    await API.put(`/advogado/editar/${id}`, request);
  } catch (error) {
    console.error(error)
    console.log("Não foi possível editar")
  }
}

export async function getClientesFromAdvogado(id: number | undefined) {
  try {
    const response = await API.get(`/advogado/listarClientes/${id}`)
    return response;
  } catch (error) {
    console.error(error)
    console.log("Não houve retorno!")
  }
}

