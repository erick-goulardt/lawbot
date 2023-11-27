import { useAuth } from "../context/AuthContext"; 
import { PrivateRoutes } from "./private.routes";
import { PublicRoutes } from "./public.routes";

export function AppRoutes() {
  const isAuth = useAuth();
  return isAuth.isAuthenticated() ? <PrivateRoutes/> : <PublicRoutes/>
}