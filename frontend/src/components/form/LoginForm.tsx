import { FormEvent, useState } from "react";
import { Form } from "./Form";
import { Input } from "../input/Input";
import { Button } from "../button/Button";
import Icon from "../../assets/teste-removebg-preview.png";
import { login } from "../../api/auth.service";
import { useAuth } from "../../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { Loading } from "../loading/Loading";
import "../../components/modal/Modal.style.css"
import "../input/Input.style.css"

function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const { login: authLogin } = useAuth();
  const navigate = useNavigate();
  const [isLoading, setLoading] = useState(false);
  const [isWrong, setWrong] = useState(false);


  const handleLogin = async (e: FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setLoading(true);
    try {
      const response = await login(email, password);
      if (response) {
        authLogin(response.data);
        localStorage.setItem("user", JSON.stringify(response.data));
        navigate("/meu-perfil");
      } else {
        setWrong(true);
      }
    } catch (error) {
      setWrong(false);
      console.error("Login failed", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      {isLoading ? (
        <Loading />
      ) : (
        <Form onSubmit={handleLogin}>
          <img src={Icon} alt="" className="mb-6" />
          <Input
            name="email"
            type="email"
            className="pb-1 pt-1 border-solid border-2 mb-3 pl-1 rounded-lg mt-2 input-login"
            label="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Insira seu email"
          />
          <Input
            name="password"
            type="password"
            className="pb-1 pt-1 border-solid border-2 mb-3 pl-1 rounded-lg mt-2 input-login"
            label="Senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Insira sua senha"
          />
          { isWrong && <p className="text-red-800 mb-4">Usuário inválido, verifique email e senha</p>}
          <Button
            type="submit"
            className="bg-blue-900 pb-3 pt-3 w-3/5 text-white rounded-lg mt-4"
          >
            Entrar
          </Button>
        </Form>
      )}
    </>
  );
}

export default LoginForm;
