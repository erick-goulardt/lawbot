import Email from "../../assets/mail.png";
import Trash from "../../assets/trash-bin.png";
import Edit from "../../assets/pen.png";
import { useState } from "react";
import "../list/List.style.css";

export interface Processo {
  id: number;
  numProcesso: string;
  status: string;
  dataAtualizacao: string;
  descricao: string;
  nomeReu: string[];
  nomeAutor: string[];
}

interface ProcessoListProps {
  processos: Processo[];
  onEditClick: () => void;
  onDeleteClick: () => void;
  onEmailClick: () => void;
  searchTerm: string;
}

export function ProcessoList({
  processos,
  onEditClick,
  onDeleteClick,
  onEmailClick,
}: ProcessoListProps) {
  console.log("processos:", processos);
  const [currentPage, setCurrentPage] = useState(1);
  const processosPerPage = 10;

  const indexOfLastProcesso = currentPage * processosPerPage;
  const indexOfFirstProcesso = indexOfLastProcesso - processosPerPage;

  const currentProcessos = processos.slice(indexOfFirstProcesso, indexOfLastProcesso);
  const totalPages = Math.ceil(currentProcessos.length / processosPerPage);

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
    <div>
      <ul className="w-5/6 h-auto border-2 rounded-xl m-auto border-blue-400 flex justify-around text-center flex-col font-breeSerif overflow-y-auto scroll-smooth mb-44">
        {currentProcessos.map((processo) => (
          <li
            key={processo.id}
            className="flex justify-around w-full mt-4 text-center border-b-2"
          >
            <div className="w-1/5 mt-1">{processo.numProcesso}</div>
            <div className="w-1/5 mt-1">{processo.dataAtualizacao}</div>
            <div className="w-1/5 mt-1">{processo.status}</div>
            <div className="w-1/5 mt-1">{processo.nomeAutor}</div>
            <div className="w-1/5 mt-1">{processo.nomeReu}</div>
            <div className="flex mr-5">
              <button onClick={() => onEditClick()}>
                <img
                  width="35"
                  className="mb-1"
                  height="35"
                  src={Edit}
                  alt="external-Edit-vkontakte-others-inmotus-design"
                />
              </button>
              <button
                className="ml-1"
                onClick={() => onDeleteClick()}
              >
                <img
                  width="35"
                  height="35"
                  className="mb-1"
                  src={Trash}
                  alt="filled-trash"
                />
              </button>
              <button className="ml-1" onClick={() => onEmailClick()}>
                <img
                  width="33"
                  height="33"
                  src={Email}
                  alt="new-post"
                  className="mb-1"
                />
              </button>
            </div>
          </li>
        ))}
        <div className="flex justify-center mt-4">{renderPageNumbers()}</div>
      </ul>
    </div>
  );
}
