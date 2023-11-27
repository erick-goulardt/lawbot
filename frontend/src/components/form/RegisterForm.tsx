import { ChangeEvent, FormEvent, useState } from "react";
import { Input } from "../input/Input";
import { Button } from "../button/Button";
import { RegisterBase } from "./RegisterBase";
import { register } from "../../api/auth.service";
import { useNavigate } from "react-router-dom";
import "../../components/modal/Modal.style.css"


export function RegisterForm() {

  const [isModalOpen, setModalOpen] = useState(false);
  const [isWrong, setWrong] = useState(false);

  const navigate = useNavigate();
  const initialUserState = {
    nome: "",
    email: "",
    senha: "",
    oab: "",
    cpf: "",
    dataNasc: "",
  };

  const [user, setUser] = useState(initialUserState);

  const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setUser((prevUser) => ({
      ...prevUser,
      [name]: value,
    }));
  };


  const handleRegister = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
      try {
        const response = await register(user.nome, user.email, user.senha, user.oab, user.cpf, user.dataNasc);
        if (response) {
          setModalOpen(true);
          console.log("Register successful", response);
        } else {
          setWrong(true)
        }
      } catch (error) {
        setWrong(false);
        navigate('/register')
        console.error("Register failed", error);
      }
  }

  const handleConfirm = () => {
    navigate('/login')
  }

  return (
    <>
      <RegisterBase onSubmit={handleRegister}>
        <Input
          name="email"
          type="email"
          className="pb-1 pt-1 border-solid border-2 mb-3 pl-2 rounded-lg input-login"
          label="Email"
          value={user.email}
          onChange={handleChange}
          placeholder="Insira seu email"
        />
        <Input
          name="nome"
          type="text"
          className="pb-1 pt-1 border-solid border-2 mb-3 pl-2 rounded-lg input-login"
          label="Name"
          value={user.nome}
          onChange={handleChange}
          placeholder="Insira seu nome"
        />
        <Input
          name="senha"
          type="password"
          className="pb-1 pt-1 border-solid border-2 mb-3 pl-2 rounded-lg input-login"
          label="Senha"
          value={user.senha}
          onChange={handleChange}
          placeholder="Insira sua senha"
        />
        <Input
          name="cpf"
          type="text"
          className="pb-1 pt-1 border-solid border-2 mb-3 pl-2 rounded-lg input-login"
          label="CPF"
          value={user.cpf}
          onChange={handleChange}
          placeholder="Insira seu CPF"
        />
        <Input
          name="oab"
          type="text"
          className="pb-1 pt-1 border-solid border-2 mb-3 pl-2 rounded-lg input-login"
          label="OAB"
          value={user.oab}
          onChange={handleChange}
          placeholder="Insira sua OAB"
        />
        <Input
          name="dataNasc"
          type="date"
          className="pb-1 pt-1 border-solid border-2 mb-3 pl-2 rounded-lg input-login"
          label="Data Nasc"
          value={user.dataNasc}
          onChange={handleChange}
          placeholder="Insira sua data de Nascimento"
        />
         { isWrong && <p className="text-red-800 mb-4">Não foi possível cadastrar, tente de novo</p>}
        <Button
          type="submit"
          className="bg-slate-400 pb-3 pt-3 w-3/5 text-white rounded-lg mt-4"
        >
          Registrar
        </Button>
      </RegisterBase>
      {isModalOpen && (
        <div className="modal">
          <div className="modal-content-register">
            <div className="modal-title">
              <h4>Você se cadastrou com sucesso!</h4>
            </div>
            <div className="modal-buttons">
              <div className="modal-input">
              </div>
              <button onClick={handleConfirm}>Ok</button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
