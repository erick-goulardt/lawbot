import Lupa from "../../assets/lupa.png";
import Plus from "../../assets/adicionar-usuario.png";
import Historico from "../../assets/pergaminho.png";
import { useState } from "react";
import "../list/List.style.css";
import Mail from "../../assets/mail.png"

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
  classe: string;
  assunto: string;
  dataAtualizacao: string;
  descricao: string;
  nomeReu: Reu[];
  nomeAutor: Autor[];
}

interface ProcessoListProps {
  processos: Processo[];
  onViewClick: (processo: Processo) => void;
  onSetClienteClick?: (processo: Processo) => void;
  onShowHistorico: (processo: Processo[]) => void;
  onEmailClick?: (processo: Processo) => void;
  showAddClientIcon?: boolean;
}

export function ProcessoList({
  processos,
  onViewClick,
  onSetClienteClick,
  onShowHistorico,
  onEmailClick,
  showAddClientIcon = true,
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

  function formatarProcesso(numero: string) {
    const numerosProcesso = numero.replace(/\D/g, "");
    const numeroFormatado = numerosProcesso.replace(
      /^(\d{7})(\d{2})(\d{4})(\d{1,7})(\d{2})(\d{4})$/,
      "$1-$2.$3.$4-$5.$6"
    );
    return numeroFormatado;
  }

  return (
    <div className="overflow-y-auto">
      <ul className="w-5/6 h-auto border-2 rounded-xl m-auto border-blue-400 flex justify-around text-center flex-col font-breeSerif scroll-smooth mb-44">
        {currentProcessos.map((processo) => (
          <li
            key={processo.id}
            className="flex justify-around w-full mt-4 text-center border-b-2"
          >
            <div className="w-1/5 mt-1">{formatarProcesso(processo.numProcesso)}</div>
            <div className="w-1/5 mt-1">{processo.dataAtualizacao}</div>
            <div className="w-1/5 mt-1">{processo.status}</div>
            <div className="w-1/5 mt-1">{processo.nomeAutor.map((autor) => autor.nome).join()}</div>
            <div className="w-1/5 mt-1">{processo.nomeReu.map((autor) => autor.nome).join()}</div>
            <div className="flex mr-5">
              <button className="mr-1" onClick={() => onShowHistorico([processo])}>
                <img
                  width="33"
                  height="33"
                  src={Historico}
                  alt="new-post"
                  className="mb-1"
                />
              </button>
              <button onClick={() => onViewClick(processo)}>
                <img
                  width="35"
                  className="mb-1"
                  height="35"
                  src={Lupa}
                  alt="external-Edit-vkontakte-others-inmotus-design"
                />
              </button>
              {showAddClientIcon ? (
                <button className="ml-1" onClick={() => onSetClienteClick && onSetClienteClick(processo)}>
                  <img
                    width="33"
                    height="33"
                    src={Plus}
                    alt="new-post"
                    className="mb-1"
                  />
                </button>
              ) : (
                <button className="ml-1" onClick={() => onEmailClick && onEmailClick(processo)}>
                <img
                  width="33"
                  height="33"
                  src={Mail}
                  alt="new-post"
                  className="mb-1"
                />
              </button>
              )}
            </div>
          </li>
        ))}
        <div className="flex justify-center w-full mt-4">{renderPageNumbers()}</div>
      </ul>
    </div>
  );
}
