import Lupa from "../../assets/lupa.png";
import Trash from "../../assets/trash-bin.png";
import Email from "../../assets/mail.png";
import Plus from "../../assets/adicionar-usuario.png";
import { useState } from "react";
import "../list/List.style.css";

interface Reu {
  nome: string;
}

interface Autor {
  nome: string;
}

interface Processo {
  id: number;
  numProcesso: string;
  status: string;
  classe: string,
  assunto: string;
  dataAtualizacao: string;
  descricao: string;
  nomeReu: Reu[];
  nomeAutor: Autor[];
}

interface ProcessoListProps {
  processos: Processo[];
  onViewClick: (processo: Processo) => void;
  onDeleteClick: (id: number) => void;
  onSetClienteClick: (processo: Processo) => void;
  onSendDescClick: (processo: Processo) => void;
}

export function ProcessoList({
  processos,
  onViewClick,
  onDeleteClick,
  onSetClienteClick,
  onSendDescClick,
}: ProcessoListProps) {
  const [currentPage, setCurrentPage] = useState(1);
  const processosPerPage = 10;

  const indexOfLastProcesso = currentPage * processosPerPage;
  const indexOfFirstProcesso = indexOfLastProcesso - processosPerPage;

  const currentProcessos = processos.slice(
    indexOfFirstProcesso,
    indexOfLastProcesso
  );

  const totalPages = Math.ceil(processos.length / processosPerPage);

  const renderPageNumbers = () => {
    const pageNumbers = [];
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(
        <button
          key={i}
          className={`mr-2 border mb-2 px-3 py-1 rounded ${
            currentPage === i ? "bg-blue-400 text-white" : "border-blue-400"
          }`}
          onClick={() => setCurrentPage(i)}
        >
          {i}
        </button>
      );
    }
    return pageNumbers;
  };

  return (
    <div className="overflow-y-auto">
      <ul className="w-5/6 h-auto border-2 rounded-xl m-auto border-blue-400 flex justify-around text-center flex-col font-breeSerif scroll-smooth mb-44">
        {currentProcessos.map((processo) => (
          <li
            key={processo.id}
            className="flex justify-around w-full mt-4 text-center border-b-2"
          >
            <div className="w-1/5 mt-1">{processo.numProcesso}</div>
            <div className="w-1/5 mt-1">{processo.dataAtualizacao}</div>
            <div className="w-1/5 mt-1">{processo.status}</div>
            <div className="w-1/5 mt-1">{processo.nomeAutor.map((autor) => autor.nome).join()}</div>
            <div className="flex mr-5">
              <button onClick={() => onViewClick(processo)}>
                <img
                  width="35"
                  className="mb-1"
                  height="35"
                  src={Lupa}
                  alt="external-Edit-vkontakte-others-inmotus-design"
                />
              </button>
              <button className="ml-1" onClick={() => onDeleteClick(processo.id)}>
                <img
                  width="35"
                  height="35"
                  className="mb-1"
                  src={Trash}
                  alt="filled-trash"
                />
              </button>
              <button className="ml-1" onClick={() => onSendDescClick(processo)}>
                <img
                  width="33"
                  height="33"
                  src={Email}
                  alt="new-post"
                  className="mb-1"
                />
              </button>
              <button className="ml-1" onClick={() => onSetClienteClick(processo)}>
                <img
                  width="33"
                  height="33"
                  src={Plus}
                  alt="new-post"
                  className="mb-1"
                />
              </button>
            </div>
          </li>
        ))}
        <div className="flex justify-center w-full mt-4">{renderPageNumbers()}</div>
      </ul>
    </div>
  );
}
