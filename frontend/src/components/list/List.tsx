import Email from "../../assets/mail.png";
import Trash from "../../assets/trash-bin.png";
import Edit from "../../assets/pen.png";
import { useState } from "react";
import "../list/List.style.css"
import { Cliente } from "../../types/Types";

interface ClienteListProps {
  clientes: Cliente[];
  onEditClick: (cliente:Cliente) => void;
  onDeleteClick: (id: number) => void;
  onEmailClick: (cliente:Cliente) => void;
  searchTerm: string;
}

function formatarCPF(cpf: string) {
  const numerosCPF = cpf.replace(/\D/g, "");
  return numerosCPF.replace(/^(\d{3})(\d{3})(\d{3})(\d{2})$/, "$1.$2.$3-$4");
}

export function ClienteList({
  clientes,
  onEditClick,
  onDeleteClick,
  onEmailClick,
  searchTerm,
}: ClienteListProps) {
  const [currentPage, setCurrentPage] = useState(1);
  const clientsPerPage = 10;

  const filteredClientes = clientes.filter((cliente) =>
    cliente.nome.toLowerCase().includes(searchTerm.toLowerCase())
  );

  const indexOfLastClient = currentPage * clientsPerPage;
  const indexOfFirstClient = indexOfLastClient - clientsPerPage;
  const currentClients = filteredClientes.slice(indexOfFirstClient, indexOfLastClient);

  const totalPages = Math.ceil(filteredClientes.length / clientsPerPage);

  const renderPageNumbers = () => {
    const pageNumbers = [];
    for (let i = 1; i <= totalPages; i++) {
      pageNumbers.push(
        <button
          key={i}
          className={`mr-2 border mb-2 px-3 py-1 rounded ${currentPage === i ? 'bg-blue-400 text-white' : 'border-blue-400'}`}
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
        {currentClients.map((cliente) => (
          <li
            key={cliente.id}
            className="flex justify-around w-full mt-4 text-center border-b-2"
          >
            <div className="w-1/3 mt-1">{cliente.nome}</div>
            <div className="w-1/3 mt-1">{cliente.email}</div>
            <div className="w-1/3 mt-1">{formatarCPF(cliente.cpf)}</div>
            <div className="flex mr-5">
             <button onClick={() => onEditClick(cliente)}>
                <img
                  width="35"
                  className="mb-1"
                  height="35"
                  src={Edit}
                  alt="external-Edit-vkontakte-others-inmotus-design"
                />
              </button>
              <button className="ml-1" onClick={() => onDeleteClick(cliente.id)}>
                <img
                  width="35"
                  height="35"
                  className="mb-1"
                  src={Trash}
                  alt="filled-trash"
                />
              </button>
              <button className="ml-1" onClick={() => onEmailClick(cliente)}>
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
        <div className="flex justify-center mt-4">
        {renderPageNumbers()}
      </div>
      </ul>
    </div>
  );
}

