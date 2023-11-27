import { API } from "../api/Api";

export async function login(login: string, senha: string) {
  try {
    const response = await API.post(
      `/login`,
      { login, senha }
    );
    return response;
  } catch (error) {
    console.error(error);
    return null;
  }
}

export async function register(nome: string, email: string, senha: string, oab: string, cpf: string, dataNascimento: string) {
  try {
    const data = await API.post(
      `/advogado/cadastro`,
      { nome, email, senha, oab, cpf, dataNascimento }
    );
    return data;
  } catch (error) {
    console.error(error);
    return null;
  }
}