import React, { useEffect, useState } from "react";
import { IProfile, getProfile, updateProfile } from "../api/advogado.service";
import { Navbar } from "../components/nav/Navbar";
import { Footer } from "../components/footer/Footer";
import { deleteProfile } from "../api/advogado.service";
import { Input } from "../components/input/Input";
import { ProcessoList } from "../components/list/ProcessoList";
import {
  IProcesso,
  cadastrarProcessosEmBloco,
  retornaProcessos,
} from "../api/processo.service";
import { useNavigate } from "react-router-dom";
import Dropzone from "react-dropzone";
import "../index.css";
import { useAuth } from "../context/AuthContext";
import { ProcessoDetailsModal } from "../components/modal/SelectedProcess";

export function ProcessosPage() {
  const [profileData, setProfileData] = useState<IProfile | null>();
  const user = useAuth();
  const [showDropdown, setShowDropdown] = useState(false);
  const [modal, setModal] = useState(false);
  const [modalDelete, setModalDelete] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  //const [existProc, setExistProc] = useState(false);
  const [selectedProcess, setSelectedProcess] = useState<IProcesso | null>(
    null
  );
  const [processos, setProcessos] = useState<IProcesso[]>([]);

  const [modalManual, setModalManual] = useState(false);
  const [uploadedFile, setUploadedFile] = useState<File | null>(null);
  const navigate = useNavigate();

  const [modalCadastro, setModalCadastro] = useState(false);

  const handleCadastrarProcesso = () => {
    setModalCadastro(!modalCadastro);
  };

  const handleShowProc = (processo: IProcesso) => {
    setSelectedProcess(processo);
  };

  const [formData, setFormData] = useState({
    nome: "",
    email: "",
    senha: "",
  });

  useEffect(() => {
    const loadProcessos = async () => {
      try {
        if (user.user?.id) {
          const response = await retornaProcessos(user.user.id);
          if (response) {
            setProcessos(response.data);
          }
        }
      } catch (error) {
        console.error("Erro ao carregar clientes", error);
      }
    };
    // setExistProc(true)
    loadProcessos();
  }, [user.user?.id, processos.values]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const requestData = {
      nome: formData.nome,
      email: formData.email,
      senha: formData.senha,
    };
    if (user.user?.id) {
      await updateProfile(user.user.id, requestData);
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

  const onDrop = (acceptedFiles: File[]) => {
    setUploadedFile(acceptedFiles[0]);
  };

  const handleFileUpload = async () => {
    if (user.user?.id && uploadedFile) {
      try {
        await cadastrarProcessosEmBloco(uploadedFile, user.user.id);
        setModalManual(false);
      } catch (error) {
        console.error("Erro ao enviar arquivo:", error);
      }
    }
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
      <div className="container-main flex w-screen items-center mt-12">
        <div className="ml-36 w-5/6 flex">
          <button
            onClick={handleCadastrarProcesso}
            className="bg-slate-100 hover:bg-slate-200 rounded-xl p-2 font-breeSerif ml-4 mb-7"
          >
            Cadastrar Processo
          </button>
          <button
            onClick={() => setModalManual(true)}
            className="bg-slate-100 hover:bg-slate-200 rounded-xl p-2 font-breeSerif ml-4 mr-5 mb-7"
          >
            Inserir Arquivo
          </button>
        </div>
      </div>
      <ProcessoList
        processos={Array.isArray(processos) ? processos : []}
        onViewClick={handleShowProc}
        onDeleteClick={() => console.log("gu")}
        onSendDescClick={() => console.log("gu")}
        onSetClienteClick={() => console.log("gu")}
      />
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
                <button onClick={() => setModalDelete(false)}>Voltar</button>
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
        <div className="modal">
          <div className="modal-content">
            <div
              className="font-breeSerif cursor-pointer"
              onClick={() => setModalManual(false)}
            >
              X
            </div>

            <div className="modal-title flex flex-col items-center justify-center">
              <h4 className="font-breeSerif text-center">Inserir Arquivo</h4>
              <Dropzone onDrop={onDrop}>
                {({ getRootProps, getInputProps }) => (
                  <div {...getRootProps()} className="dropzone">
                    <input {...getInputProps()} />
                    <p className="mt-3">
                      Arraste e solte um arquivo aqui, ou clique para selecionar
                      um arquivo
                    </p>
                  </div>
                )}
              </Dropzone>
              <div className="button-form">
                <button onClick={handleFileUpload}>Enviar Arquivo</button>
                <button onClick={() => setModalManual(false)}>Cancelar</button>
              </div>
            </div>
          </div>
        </div>
      )}
      {modalCadastro && (
        <div className="modal">
          <div className="modal-content">
            <div className="button-form">
              <button onClick={console.log}>Cadastrar Processo</button>
              <button onClick={console.log}>Cancelar</button>
            </div>
          </div>
        </div>
      )}
      {selectedProcess && (
        <ProcessoDetailsModal
          processo={selectedProcess}
          onClose={() => setSelectedProcess(null)}
        />
      )}
    </>
  );
}
