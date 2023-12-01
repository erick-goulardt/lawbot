import React, { useEffect, useState } from "react";
import { getProfile, updateProfile } from "../api/advogado.service";
import { Navbar } from "../components/nav/Navbar";
import { Footer } from "../components/footer/Footer";
import { deleteProfile } from "../api/advogado.service";
import { Input } from "../components/input/Input";
import { ProcessoList } from "../components/list/ProcessoList";
import {
  cadastrarProcessosEmBloco,
  definirCliente,
  getProcessoComCliente,
  getProcessoSemCliente,
  registrarProcessoManual,
  setDescricaoProc,
} from "../api/processo.service";
import { useNavigate } from "react-router-dom";
import Dropzone from "react-dropzone";
import "../index.css";
import { useAuth } from "../context/AuthContext";
import { ProcessoDetailsModal } from "../components/modal/SelectedProcess";
import CustomModal from "../components/modal/ChooseClienteModal";
import { IProcesso, IProfile } from "../types/Types";

export function ProcessosPage() {
  const [profileData, setProfileData] = useState<IProfile | null>();
  const user = useAuth();
  const [showDropdown, setShowDropdown] = useState(false);
  const [modal, setModal] = useState(false);
  const [modalDelete, setModalDelete] = useState(false);
  const [modalEdit, setModalEdit] = useState(false);
  const [selectedProcess, setSelectedProcess] = useState<IProcesso | null>(
    null
  );
  const [selectedProcessForModal, setSelectedProcessForModal] =
    useState<IProcesso | null>(null);
  const [processos, setProcessos] = useState<IProcesso[]>([]);
  const [isClicked, setIsClicked] = useState(false);
  const [emailModalOpen, setEmailModalOpen] = useState(false);
  const [selectedProcessForEmailModal, setSelectedProcessForEmailModal] =
    useState<IProcesso | null>(null);
  const [modalManual, setModalManual] = useState(false);
  const [uploadedFile, setUploadedFile] = useState<File | null>(null);
  const [isAddClientModalOpen, setAddClientModalOpen] = useState(false);
  const [emailContent, setEmailContent] = useState("");
  const [isHistoricoModalOpen, setHistoricoModalOpen] = useState(false);
  const [historicModal, setHistoricModal] = useState<IProcesso | null>();
  const [modalCadastraProc, setModalCadastraProc] = useState(false);
  const [registroProc, setRegistroProc] = useState({
    ultimoEvento: "",
    dataAtualizacao: "",
    descricao: "",
    numeroProcesso: "",
    classe: "",
    localidade: "",
    assunto: "",
    nomeReu: {
      nome: "",
    },
    nomeAutor: {
      nome: "",
    },
  });

  const handleShowHistoricoModal = (processo: IProcesso) => {
    setHistoricModal(processo);
    setHistoricoModalOpen(true);
  };

  const handleModalCadastro = () => {
    setModalCadastraProc(!modalCadastraProc);
    resetFormProc();
  };

  const resetFormProc = () => {
    if (modalManual === false) {
      setRegistroProc({
        ultimoEvento: "",
        dataAtualizacao: "",
        descricao: "",
        numeroProcesso: "",
        classe: "",
        localidade: "",
        assunto: "",
        nomeReu: {
          nome: "",
        },
        nomeAutor: {
          nome: "",
        },
      });
    }
  };

  const handleSubmitCadastro = async (e: { preventDefault: () => void }) => {
    e.preventDefault();

    try {
      await registrarProcessoManual(
        user.user?.id,
        0,
        registroProc.ultimoEvento,
        registroProc.dataAtualizacao,
        registroProc.descricao,
        registroProc.numeroProcesso,
        registroProc.classe,
        registroProc.localidade,
        registroProc.assunto,
        registroProc.nomeReu,
        registroProc.nomeAutor
      );
    } catch (error) {
      console.error("Register failed", error);
    }
    handleModalCadastro();
  };

  const handleRegisterProc = (
    e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>
  ) => {
    const { name, value } = e.target;
    setRegistroProc({
      ...registroProc,
      [name]: value,
    });
  };

  const navigate = useNavigate();

  const handleShowProc = (processo: IProcesso) => {
    setSelectedProcess(processo);
  };

  const [formData, setFormData] = useState({
    nome: "",
    email: "",
    senha: "",
  });

  const openEmailModal = (processo: IProcesso) => {
    setEmailModalOpen(!emailModalOpen);
    setSelectedProcessForEmailModal(processo);
  };

  const closeEmailModal = () => {
    setEmailModalOpen(false);
    setSelectedProcessForEmailModal(null);
    setEmailContent("");
  };

  const handleEmailSubmit = async () => {
    try {
      const response = await setDescricaoProc({
        processo: selectedProcessForEmailModal,
        descricao: emailContent,
      });
      if (response) {
        console.log("E-mail enviado com sucesso!");
        closeEmailModal();
      } else {
        console.error("Erro ao enviar e-mail. Resposta nula.");
      }
    } catch (error) {
      console.error("Erro ao enviar e-mail:", error);
    }
  };

  useEffect(() => {
    const loadProcessos = async () => {
      try {
        if (user.user?.id) {
          let response;
          if (isClicked) {
            response = await getProcessoComCliente(user.user.id);
          } else {
            response = await getProcessoSemCliente(user.user.id);
          }
          if (response) {
            setProcessos(response.data);
          }
        }
      } catch (error) {
        console.error("Erro ao carregar processos", error);
      }
    };
    loadProcessos();
  }, [user.user?.id, isClicked]);

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

  const handleClicked = () => {
    setIsClicked(!isClicked);
  };

  const handleAddClientClick = () => {
    setAddClientModalOpen(!isAddClientModalOpen);
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
            onClick={handleModalCadastro}
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
          <div className="bg-slate-100 hover:bg-slate-200 rounded-xl p-2 flex h-10">
            <h2 className="font-breeSerif mr-2 h-fit">Mostrar com Clientes</h2>
            <button className="switch mr-2" onClick={handleClicked}>
              <input type="checkbox" checked={isClicked} />
              <span className="slider round"></span>
            </button>
          </div>
        </div>
      </div>
      <ul className="w-5/6 h-auto border-2 rounded-xl m-auto border-blue-400 flex justify-around text-center font-breeSerif scroll-smooth mb-10">
        <div className="w-1/5 mt-1">N° Processo</div>
        <div className="w-1/5 mt-1">Data Atualização</div>
        <div className="w-1/5 mt-1">Status</div>
        <div className="w-1/5 mt-1">Autores</div>
        <div className="w-1/5 mt-1 mr-36">Reus</div>
      </ul>
      <ProcessoList
        processos={Array.isArray(processos) ? processos : []}
        onViewClick={handleShowProc}
        onSetClienteClick={(processo) => {
          setAddClientModalOpen(true);
          setSelectedProcessForModal(processo);
        }}
        onShowHistorico={handleShowHistoricoModal}
        showAddClientIcon={!isClicked}
        onEmailClick={(processo) => {
          openEmailModal(processo);
        }}
      />
      <CustomModal
        isOpen={isAddClientModalOpen}
        onClose={handleAddClientClick}
        onSave={(data) => {
          const clienteSent = {
            id: data.selectedOption?.value,
            processo: selectedProcessForModal,
          };
          definirCliente(clienteSent);
        }}
        idAdvogado={profileData?.id}
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
                  <div className="mb-3 ml-10 pl-2">
                    <p className="font-breeSerif">Data Nascimento:</p>
                    {profileData?.dataNascimento}
                  </div>
                  <div className="mb-3 pr-24 mr-4">
                    <p className="font-breeSerif">CPF:</p>
                    {profileData?.cpf}
                  </div>
                </div>
                <div className="section-input ml-7">
                  <div className="mb-3">
                    <p className="font-breeSerif">OAB:</p>
                    {profileData?.oab}
                  </div>
                  <div className="mb-1 ">
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
        <div className="modal-overlay">
          <div className="modal-content">
            <div className="flex flex-col items-center justify-center">
              <div
                className="close-button flex justify-start w-full"
                onClick={() => setModalManual(false)}
              >
                <p className="cursor-pointer">X</p>
                <h4 className="modal-title-text text-center text-1xl ml-5 pb-5">
                  Inserir Arquivo
                </h4>
              </div>

              <Dropzone onDrop={onDrop}>
                {({ getRootProps, getInputProps }) => (
                  <div {...getRootProps()} className="dropzone">
                    <input {...getInputProps()} />
                    <p className="dropzone-text text-xl text-center cursor-pointer">
                      Arraste e solte um arquivo aqui, ou clique para selecionar
                      um arquivo
                    </p>
                  </div>
                )}
              </Dropzone>
              <div className="button-form">
                <button
                  className="button-arquivo -mb-6"
                  onClick={handleFileUpload}
                >
                  Enviar Arquivo
                </button>
              </div>
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
      {emailModalOpen && (
        <div className="modal">
          <div className="modal-content">
            <span className="close cursor-pointer" onClick={closeEmailModal}>
              X
            </span>
            <h2 className="font-breeSerif text-center text-lg mb-3">
              Envie uma Atualização Processual
            </h2>
            <textarea
              className="w-full p-2 mb-2 border border-solid border-gray-300 rounded-md resize-y"
              rows={4}
              cols={47}
              placeholder="Digite o conteúdo do e-mail..."
              value={emailContent}
              onChange={(e) => setEmailContent(e.target.value)}
            ></textarea>
            <button className="botao-descricao" onClick={handleEmailSubmit}>
              Enviar Atualização
            </button>
          </div>
        </div>
      )}
      {isHistoricoModalOpen && historicModal && (
        <div className="modal-historico">
          <div className="modal-content-historico">
            <span
              className="close cursor-pointer"
              onClick={() => setHistoricoModalOpen(false)}
            >
              X
            </span>
            <h2 className="font-breeSerif text-center text-lg mb-3">
              Histórico do Processo
            </h2>
            <div className="flex text-center w-full border-b-2 pb-2">
              <p>{historicModal.descricao}</p>
              <p className="ml-3">{historicModal.dataAtualizacao}</p>
              <p className="ml-3">{historicModal.status}</p>
            </div>
          </div>
        </div>
      )}
      {modalCadastraProc && (
        <div className="modal-auto">
          <div className="modal-content-auto">
            <div className="modal-title-auto">
              <h4 className="text-center font-breeSerif">
                Cadastro Processual
              </h4>
              <h4
                className="font-breeSerif cursor-pointer"
                onClick={handleModalCadastro}
              >
                X
              </h4>
            </div>
            <div className="w-max">
              <div className="flex gap-3">
                <Input
                  label={"Último Evento"}
                  value={registroProc.ultimoEvento}
                  onChange={handleRegisterProc}
                  placeholder={"Insira o Evento"}
                  className={
                    "mb-5 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg w-56"
                  }
                  type={"text"}
                  name={"ultimoEvento"}
                />
                <Input
                  label={"Data Atualização"}
                  value={registroProc.dataAtualizacao}
                  onChange={handleRegisterProc}
                  placeholder={"Insira Data de Atualização"}
                  className={
                    "mb-5 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg w-56"
                  }
                  type={"date"}
                  name={"dataAtualizacao"}
                />
                <Input
                  label={"Descrição Processual"}
                  value={registroProc.descricao}
                  onChange={handleRegisterProc}
                  placeholder={"Descrição"}
                  className={
                    "mb-5 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg w-56"
                  }
                  type={"text"}
                  name={"descricao"}
                />
              </div>
              <div className="flex gap-3">
                <Input
                  label={"Número Processual"}
                  value={registroProc.numeroProcesso}
                  onChange={handleRegisterProc}
                  placeholder={"Número do Processo"}
                  className={
                    "mb-5 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg w-56"
                  }
                  type={"text"}
                  name={"numeroProcessual"}
                />
                <Input
                  label={"Classe Processual"}
                  value={registroProc.classe}
                  onChange={handleRegisterProc}
                  placeholder={"Classe"}
                  className={
                    "w-56 mb-8 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg"
                  }
                  type={"text"}
                  name={"classe"}
                />

                <Input
                  label={"Localidade do Processo"}
                  value={registroProc.localidade}
                  onChange={handleRegisterProc}
                  placeholder={"Localidade"}
                  className={
                    "w-56 mb-8 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg"
                  }
                  type={"text"}
                  name={"localidade"}
                />
              </div>
              <div className="flex gap-3">
                <Input
                  label={"Assunto"}
                  value={registroProc.assunto}
                  onChange={handleRegisterProc}
                  placeholder={"Assunto"}
                  className={
                    "w-56 mb-8 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg"
                  }
                  type={"text"}
                  name={"assunto"}
                />
                <Input
                  label={"Nome do Réu"}
                  value={registroProc.nomeReu.nome}
                  onChange={handleRegisterProc}
                  placeholder={"Réu"}
                  className={
                    "w-56 mb-8 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg"
                  }
                  type={"text"}
                  name={"nomeReu"}
                />
                <Input
                  label={"Nome do Autor"}
                  value={registroProc.nomeAutor.nome}
                  onChange={handleRegisterProc}
                  placeholder={"Autor"}
                  className={
                    "w-56 mb-8 pb-1 pt-1 border-solid border-2 pl-1 rounded-lg"
                  }
                  type={"text"}
                  name={"nomeAutor"}
                />
              </div>
            </div>
            <div className="modal-input-register">
              <button onClick={handleSubmitCadastro}>Salvar Processo</button>
            </div>
          </div>
        </div>
      )}
    </>
  );
}
