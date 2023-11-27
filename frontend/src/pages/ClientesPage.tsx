import { useEffect, useState } from "react";
import {
  IProfile,
  getClientesFromAdvogado,
  getProfile,
  updateProfile,
} from "../api/advogado.service";
import { Navbar } from "../components/nav/Navbar";
import { useAuth } from "../context/AuthContext";
import { Footer } from "../components/footer/Footer";
import "../index.css";
import { useNavigate } from "react-router-dom";
import "../components/modal/Modal.style.css";
import { deleteProfile } from "../api/advogado.service";
import { Input } from "../components/input/Input";
import emailjs from "@emailjs/browser";
import {
  deleteCliente,
  editCliente,
  registerCliente,
} from "../api/cliente.service";
import { Cliente, ClienteList } from "../components/list/List";

export function ClientesPage() {
  const [profileData, setProfileData] = useState<IProfile | null>();
  const user = useAuth();
  const [showDropdown, setShowDropdown] = useState(false);
  const [modal, setModal] = useState(false);
  const [modalDelete, setModalDelete] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  const navigate = useNavigate();
  const [modalManual, setModalManual] = useState(false);
  const [existList, setExistList] = useState(false);
  const [clientes, setClientes] = useState<Cliente[]>([]);
  const [searchTerm, setSearchTerm] = useState<string>("");
  const [editClienteModal, setEditClienteModal] = useState(false);
  const [emailModal, setEmailModal] = useState(false);
  const [emailMessage, setEmailMessage] = useState("");

  const [formData, setFormData] = useState({
    nome: "",
    email: "",
    senha: "",
  });

  const [clienteData, setClienteData] = useState({
    id: 0,
    nome: "",
    email: "",
    cpf: "",
  });

  const handleEmailMessageChange = (
    e: React.ChangeEvent<HTMLTextAreaElement>
  ) => {
    setEmailMessage(e.target.value);
  };

  const handleRegisterClienteChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setClienteData({
      ...clienteData,
      [name]: value,
    });
  };

  const [registroData, setRegistroData] = useState({
    nome: "",
    email: "",
    senha: "",
    cpf: "",
    dataNasc: "",
  });

  const handleRandomizerPassword = () => {
    const chars =
      "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJLMNOPQRSTUVWXYZ";
    const passwordLength = 16;
    let password = "";

    for (let i = 0; i < passwordLength; i++) {
      const randomNumber = Math.floor(Math.random() * chars.length);
      password += chars.substring(randomNumber, randomNumber + 1);
    }

    setRegistroData({
      ...registroData,
      senha: password,
    });
  };

  const handleModalManual = () => {
    setModalManual(!modalManual);
    resetForm();
  };

  const resetForm = () => {
    if (modalManual === false) {
      setRegistroData({
        nome: "",
        email: "",
        senha: "",
        cpf: "",
        dataNasc: "",
      });
    }
  };

  const handleRegisterChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setRegistroData({
      ...registroData,
      [name]: value,
    });
  };

  const handleClienteModal = (cliente: Cliente) => {
    setEditClienteModal(!editClienteModal);
    setClienteData({
      id: cliente.id,
      nome: cliente.nome,
      email: cliente.email,
      cpf: cliente.cpf,
    });
  };

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

  const handleEmailModal = (cliente: Cliente) => {
    setClienteData({
      id: cliente.id,
      nome: cliente.nome,
      email: cliente.email,
      cpf: cliente.cpf,
    });
    setEmailMessage(`Prezado(a) ${cliente.nome}, \n\n`);

    setEmailModal(!emailModal);
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

  const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
    setSearchTerm(e.target.value);
  };

  const handleSendEmail = async (e: React.FormEvent) => {
    e.preventDefault();

    const serviceId = "service_oxtseai";
    const templateId = "template_2yewhov";
    const publicKey = "1FLvzQdxnhnrEA9RO";

    const templateParams = {
      from_name: "Lawbot",
      from_email: profileData?.email,
      to_name: clienteData.nome,
      email_to: clienteData.email,
      message: `Your email message goes here`,
    };

    try {
      await emailjs.send(serviceId, templateId, templateParams, publicKey);
      console.log("Email sent successfully!");
    } catch (error) {
      console.error("Error sending email:", error);
    }

    setEmailModal(!emailModal);
  };

  const handleSubmitEmail = async (e: { preventDefault: () => void }) => {
    e.preventDefault();
    const serviceId = "service_oxtseai";
    const templateId = "template_2yewhov";
    const publicKey = "1FLvzQdxnhnrEA9RO";

    const templateParams = {
      from_name: "Lawbot",
      from_email: "somoslawbot@gmail.com",
      to_name: registroData.nome,
      email_to: registroData.email,
      message: `Você está cadastrado no nosso sistema, para acessá-lo aqui está suas credenciais!
      Login: ${registroData.email}
      Senha: ${registroData.senha}`,
    };

    emailjs
      .send(serviceId, templateId, templateParams, publicKey)
      .then(() => {
        console.log("Email sent successfully!");
      })
      .catch(() => {
        console.error("Error sending email:");
      });

    try {
      await registerCliente(
        user.user?.id,
        registroData.nome,
        registroData.email,
        registroData.senha,
        registroData.cpf,
        registroData.dataNasc
      );
    } catch (error) {
      console.error("Register failed", error);
    }
    handleModalManual();
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

  useEffect(() => {
    const loadClientes = async () => {
      try {
        if (user.user?.id) {
          const response = await getClientesFromAdvogado(user.user.id);
          if (response) {
            setClientes(response.data);
            setExistList(true);
          }
        }
      } catch (error) {
        console.error("Erro ao carregar clientes", error);
      }
    };

    loadClientes();
  }, [clienteData, clientes.length, user.user?.id]);

  const handleDeleteCliente = async (id: number) => {
    try {
      await deleteCliente(id);
      if (user.user?.id) {
        const response = await getClientesFromAdvogado(user.user?.id);
        if (response) {
          setClientes(response.data);
        }
      }
    } catch (error) {
      console.error("Erro ao excluir cliente", error);
    }
  };

  const handleEditCliente = async (id: number) => {
    try {
      await editCliente(id, {
        nome: clienteData.nome,
        email: clienteData.email,
        cpf: clienteData.cpf,
      });

      if (user.user?.id) {
        const response = await getClientesFromAdvogado(user.user?.id);
        if (response) {
          setClientes(response.data);
        }
      }
    } catch (error) {
      console.error("Erro ao editar cliente", error);
    }
    setEditClienteModal(!editClienteModal);
  };

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
      <div className="container-main flex w-screen items-center mt-12">
        <div className="ml-36 w-5/6 flex justify-between">
          <button
            onClick={handleModalManual}
            className="bg-slate-100 hover:bg-slate-200 rounded-xl p-2 font-breeSerif ml-4 mb-7"
          >
            Cadastrar Cliente
          </button>
          <input
            type="text"
            placeholder="Pesquisar cliente..."
            value={searchTerm}
            onChange={handleSearch}
            className="border rounded-xl h-10 p-1"
          />
        </div>
      </div>

      {existList ? (
        <>
          <ClienteList
            clientes={clientes}
            onEditClick={handleClienteModal}
            onDeleteClick={handleDeleteCliente}
            onEmailClick={handleEmailModal}
            searchTerm={searchTerm}
          />
        </>
      ) : (
        <div className="w-5/6 h-96 border-2 rounded-xl m-auto border-blue-400 flex justify-center items-center">
          <div>
            <h1 className="font-breeSerif text-4xl">
              Nenhum cliente cadastrado ainda!
            </h1>
          </div>
        </div>
      )}
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
                <div className="section-input mb-3">
                  <div className="mb-3 pl-5">
                    <p className="font-breeSerif">Data Nascimento:</p>
                    {profileData?.dataNascimento}
                  </div>
                  <div className="mb-3 pr-11">
                    <p className="font-breeSerif">CPF:</p>
                    {profileData?.cpf}
                  </div>
                </div>
                <div className="section-input">
                  <div className="mb-3">
                    <p className="font-breeSerif">OAB:</p>
                    {profileData?.oab}
                  </div>
                  <div className="mb-1">
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
      {modalManual && (
        <div className="modal-auto">
          <div className="modal-content-auto">
            <div className="modal-title-auto">
              <h4 className="text-center font-breeSerif">
                Cadastro de Cliente
              </h4>
              <h4
                className="font-breeSerif cursor-pointer"
                onClick={handleModalManual}
              >
                X
              </h4>
            </div>
            <div>
              <div className="flex">
                <Input
                  label={"Nome"}
                  value={registroData.nome}
                  onChange={handleRegisterChange}
                  placeholder={"Insira o Nome"}
                  className={
                    "mb-5 pb-1 mr-10 pt-1 border-solid border-2 pl-1 rounded-lg w-56"
                  }
                  type={"text"}
                  name={"nome"}
                />
                <Input
                  label={"Email"}
                  value={registroData.email}
                  onChange={handleRegisterChange}
                  placeholder={"Insira o Email"}
                  className={
                    "mb-5 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg w-56"
                  }
                  type={"email"}
                  name={"email"}
                />
              </div>
              <div className="flex">
                <Input
                  label={"Data de Nascimento"}
                  value={registroData.dataNasc}
                  onChange={handleRegisterChange}
                  placeholder={"Data Nascimento"}
                  className={
                    "mb-5 mr-10 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg w-56"
                  }
                  type={"date"}
                  name={"dataNasc"}
                />
                <Input
                  label={"CPF"}
                  value={registroData.cpf}
                  onChange={handleRegisterChange}
                  placeholder={"Insira o CPF"}
                  className={
                    "mb-5 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg w-56"
                  }
                  type={"text"}
                  name={"cpf"}
                />
              </div>
              <div className="flex">
                <Input
                  label={"Senha"}
                  value={registroData.senha}
                  onChange={handleRegisterChange}
                  placeholder={"Senha Gerada"}
                  className={
                    "w-56 mb-8 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg"
                  }
                  type={"password"}
                  name={"senha"}
                />
                <img
                  width="48"
                  height="48"
                  src="https://img.icons8.com/android/48/228BE6/dice.png"
                  alt="dice"
                  className="w-5 h-5 mt-14 ml-3 cursor-pointer"
                  onClick={handleRandomizerPassword}
                />
              </div>
            </div>
            <div className="modal-input-register">
              <button onClick={handleSubmitEmail}>Salvar Cliente</button>
            </div>
          </div>
        </div>
      )}
      {editClienteModal && (
        <div className="modal-edit">
          <div className="modal-content-edit">
            <div className="modal-title-edit">
              <h4 className="text-center font-breeSerif">Edição de Cliente</h4>
            </div>
            <div className="modal-buttons-delete">
              <Input
                label={"Nome"}
                value={clienteData.nome}
                onChange={handleRegisterClienteChange}
                placeholder={"Insira o nome"}
                type={"text"}
                name={"nome"}
                className={
                  "pb-1 pt-1 border-solid border-2 mb-3 pl-1 rounded-lg w-full"
                }
              />
              <Input
                label={"Email"}
                value={clienteData.email}
                onChange={handleRegisterClienteChange}
                placeholder={"Insira o email"}
                type={"email"}
                name={"email"}
                className={
                  "pb-1 pt-1 border-solid border-2 mb-3 pl-1 rounded-lg w-full"
                }
              />
              <Input
                label={"CPF"}
                value={clienteData.cpf}
                onChange={handleRegisterClienteChange}
                placeholder={"Insira o CPF"}
                type={"text"}
                name={"cpf"}
                className={
                  "mb-5 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg w-full"
                }
              />
              <div className="modal-input-edit">
                <button onClick={() => handleEditCliente(clienteData.id)}>
                  Editar
                </button>
                <button onClick={() => handleClienteModal(clienteData)}>
                  Cancelar
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
      {emailModal && (
        <div className="modal-edit">
          <div className="modal-content-edit">
            <div className="modal-title-edit">
              <h4 className="text-center font-breeSerif">Enviar Email</h4>
            </div>
            <div className="modal-buttons-email">
              <form onSubmit={handleSendEmail}>
                <div className="mb-3">
                  <p className="font-breeSerif">De:</p>
                  <input
                    type="text"
                    value={profileData?.email || "somoslawbot@gmail.com"}
                    readOnly
                    className="border-solid border-2 rounded-lg w-full p-1"
                  />
                </div>
                <div className="mb-3">
                  <p className="font-breeSerif">Para:</p>
                  <input
                    type="text"
                    value={clienteData.email}
                    readOnly
                    className="border-solid border-2 rounded-lg w-full p-1"
                  />
                </div>
                <div className="mb-3">
                  <p className="font-breeSerif">Mensagem:</p>
                  <textarea
                    value={emailMessage}
                    onChange={handleEmailMessageChange}
                    rows={5}
                    className="border-solid border-2 rounded-lg w-full p-1"
                  />
                </div>
                <div className="modal-input-edit">
                  <button type="submit">Enviar</button>
                  <button type="button" onClick={() => setEmailModal(false)}>
                    Cancelar
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
