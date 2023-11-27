import { IProcesso } from "../../api/processo.service";

interface ProcessoDetailsModalProps {
  processo: IProcesso;
  onClose: () => void;
}

export function ProcessoDetailsModal({
  processo,
  onClose,
}: ProcessoDetailsModalProps) {
  return (
    <div className="modal">
      <div className="modal-content">
        <div className="-mt-1 font-breeSerif cursor-pointer" onClick={onClose}>
          X
        </div>
        <div className="modal-title">
          <h4 className="text-center font-breeSerif">Detalhes do Processo</h4>
        </div>
        <div className="modal-content-details">
          <p>Classe: {processo.classe}</p>
          <p>Assunto: {processo.assunto}</p>
          <p>Número do Processo: {processo.numProcesso}</p>
          <p>
            Nomes dos Autores:{" "}
            {processo.nomeAutor.map((autor) => autor.nome).join()}
          </p>
          <p>
            Nomes dos Reus: {processo.nomeReu.map((autor) => autor.nome).join()}
          </p>
          <p>Status: {processo.status}</p>
        </div>
      </div>
    </div>
  );
}
