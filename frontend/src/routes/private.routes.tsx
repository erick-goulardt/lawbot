import { Routes, Route } from "react-router-dom";
import { ProfilePage } from "../pages/ProfilePage";
import { LoginPage } from "../pages/LoginPage";
import { HomePage } from "../pages/HomePage";
import { RegisterPage } from "../pages/RegisterPage";
import { ClientesPage } from "../pages/ClientesPage";
import { ProcessosPage } from "../pages/ProcessosPage";

export function PrivateRoutes() {
  return (
    <Routes>
      <Route path="/meu-perfil" element={<ProfilePage />} />
      <Route path="/login" element={<LoginPage />} />
      <Route path="/registrar" element={<RegisterPage />} />
      <Route path="/" element={<HomePage />} />
      <Route path="/meu-perfil/clientes" element={<ClientesPage />} />
      <Route path="/meu-perfil/processos" element={<ProcessosPage />} />
    </Routes>
  );
}
