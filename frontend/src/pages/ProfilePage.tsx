import { useEffect, useState } from "react";
import { getProfile, updateProfile } from "../api/advogado.service";
import { Navbar } from "../components/nav/Navbar";
import { useAuth } from "../context/AuthContext";
import HomePhoto from "../assets/freepik.svg";
import { Footer } from "../components/footer/Footer";
import "../index.css";
import { useNavigate } from "react-router-dom";
import "../components/modal/Modal.style.css";
import { deleteProfile } from "../api/advogado.service";
import { Input } from "../components/input/Input";
import { IProfile } from "../types/Types";

export function ProfilePage() {
  const [profileData, setProfileData] = useState<IProfile | null>();
  const user = useAuth();
  const [showDropdown, setShowDropdown] = useState(false);
  const [modal, setModal] = useState(false);
  const [modalDelete, setModalDelete] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    nome: "",
    email: "",
    senha: "",
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const requestData = {
      nome: formData.nome,
      email: formData.email,
      senha: formData.senha,
    };
    if (user.user?.id) {
      updateProfile(user.user.id, requestData);
      setModalEdit(!modalEdit);
      navigate("/login");
    } else {
      console.log("Não foi possível editar!");
    }
  };

  const handleEditProfile = () => {
    setShowDropdown(!showDropdown);
    setModal(!modal);
  };

  const handleDeleteModal = () => {
    setModalDelete(!modalDelete);
    setModal(!modal);
  };

  const handleEditModal = () => {
    setModalEdit(!modalEdit);
    setModal(!modal);
  };

  const handleDropdownToggle = () => {
    setShowDropdown(!showDropdown);
  };

  const handleLogout = () => {
    user.logout();
    localStorage.removeItem("token");
    navigate("/login");
  };

  const handleDeleteAccount = () => {
    if (user.user?.id) {
      deleteProfile(user.user?.id);
      setModalDelete(!modalDelete);
      navigate("/");
      localStorage.clear();
    } else {
      console.error("Usuario não encontrado");
    }
  };

  const handleModal = () => {
    setModal(!modal);
  };

  useEffect(() => {
    const loadUserProfile = async () => {
      try {
        if (user.user?.id) {
          const profile = await getProfile(user.user.id);
          if (profile) {
            setProfileData(profile);
          }
        }
      } catch (error) {
        console.error(error);
      }
    };

    loadUserProfile();
  }, [user, user.user?.id]);

  return (
    <>
      <Navbar
        links={[
          { to: "/meu-perfil", label: "Início" },
          { to: "/meu-perfil/processos", label: "Processos" },
          { to: "/meu-perfil/clientes", label: "Clientes" },
          {
            to: "/myprofile",
            label: "Perfil ",
            onClick: handleDropdownToggle,
            dropdown: (
              <div
                className={`dropdown ${showDropdown ? "show" : ""}`}
                onClick={(e) => e.stopPropagation()}
              >
                <p className="text-center mb-3 font-breeSerif">
                  {profileData?.nome}
                </p>
                <button onClick={handleLogout}>Logout</button>
                <button onClick={handleEditProfile}>Info</button>
              </div>
            ),
          },
        ]}
      />
      <div className="container-main flex w-screen items-center mt-36">
        <div className="image-box w-2/4 flex justify-center">
          <img src={HomePhoto} alt="" />
        </div>
        <div className="motto-box w-2/4">
          <h2 className="font-breeSerif text-7xl w-3/4">
            Seja bem-vindo ao Lawbot! Experimente as novas funcionalidades!
          </h2>
        </div>
      </div>
      <Footer />
      {modal && (
        <div className="modal">
          <div className="modal-content">
            <div
              className="-mt-1 font-breeSerif cursor-pointer"
              onClick={handleModal}
            >
              X
            </div>
            <div className="modal-title flex flex-col items-center justify-center">
              <img
                className=""
                width="96"
                height="96"
                src="https://img.icons8.com/material-sharp/96/228BE6/user.png"
                alt="user"
              />
              <h4 className="font-breeSerif text-center">
                {profileData?.nome}
              </h4>
              <div className="flex mb-3">
                <img
                  className="mr-3 cursor-pointer"
                  width="24"
                  height="24"
                  src="https://img.icons8.com/material-sharp/24/228BE6/edit--v1.png"
                  alt="edit--v1"
                  onClick={handleEditModal}
                />
                <img
                  className="cursor-pointer"
                  width="24"
                  height="24"
                  src="https://img.icons8.com/material-sharp/24/228BE6/trash.png"
                  alt="trash"
                  onClick={handleDeleteModal}
                />
              </div>
            </div>
            <div className="modal-buttons">
              <div className="modal-input">
                <div className="flex">
                  <div className="mb-3">
                    <p className="font-breeSerif">Data Nascimento:</p>
                    {profileData?.dataNascimento}
                  </div>
                  <div className="mb-3 ml-3">
                    <p className="font-breeSerif">CPF:</p>
                    {profileData?.cpf}
                  </div>
                  <div className="mb-3 ml-3">
                    <p className="font-breeSerif">OAB:</p>
                    {profileData?.oab}
                  </div>
                  <div className="mb-1 ml-3">
                    <p className="font-breeSerif">Email:</p>
                    {profileData?.email}
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
      {modalDelete && (
        <div className="modal-delete">
          <div className="modal-content-delete">
            <div className="modal-title-delete">
              <h4 className="text-center ml-6 font-breeSerif">
                Deseja realmente deletar sua conta?
              </h4>
            </div>
            <div className="modal-buttons-delete">
              <div className="modal-input-delete">
                <button onClick={handleDeleteAccount}>Confirmar</button>
                <button onClick={handleDeleteModal}>Voltar</button>
              </div>
            </div>
          </div>
        </div>
      )}
      {modalEdit && (
        <div className="modal-edit">
          <div className="modal-content-edit">
            <div className="modal-title-edit">
              <h4 className="text-center font-breeSerif">Edição de Perfil</h4>
            </div>
            <div className="modal-buttons-delete">
              <Input
                label={"Nome"}
                value={formData.nome}
                onChange={handleChange}
                placeholder={"Insira seu nome"}
                type={"text"}
                name={"nome"}
                className={
                  "pb-1 pt-1 border-solid border-2 mb-3 pl-1 rounded-lg w-full"
                }
              />
              <Input
                label={"Senha"}
                value={formData.senha}
                onChange={handleChange}
                placeholder={"Insira sua nova senha"}
                type={"text"}
                name={"senha"}
                className={
                  "pb-1 pt-1 border-solid border-2 mb-3 pl-1 rounded-lg w-full"
                }
              />
              <Input
                label={"Email"}
                value={formData.email}
                onChange={handleChange}
                placeholder={"Insira seu novo email"}
                type={"email"}
                name={"email"}
                className={
                  "mb-5 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg w-full"
                }
              />
              <div className="modal-input-edit">
                <button onClick={handleSubmit}>Editar</button>
                <button onClick={handleEditModal}>Cancelar</button>
              </div>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
