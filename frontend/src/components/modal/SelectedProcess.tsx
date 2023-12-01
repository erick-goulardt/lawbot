import { IProcesso } from "../../types/Types";

interface ProcessoDetailsModalProps {
  processo: IProcesso;
  onClose: () => void;
}
function formatarProcesso(numero: string) {
  const numerosProcesso = numero.replace(/\D/g, "");
  const numeroFormatado = numerosProcesso.replace(
    /^(\d{7})(\d{2})(\d{4})(\d{1,7})(\d{2})(\d{4})$/,
    "$1-$2.$3.$4-$5.$6"
  );
  return numeroFormatado;
}

export function ProcessoDetailsModal({
  processo,
  onClose,
}: ProcessoDetailsModalProps) {
  return (
    <div className="modal">
      <div className="modal-content">
        <div className="modal-title w-full justify-between">
          <div className=" font-breeSerif cursor-pointer" onClick={onClose}>
            X
          </div>
          <h4 className="text-center font-breeSerif mb-4">
            Detalhes do Processo
          </h4>
        </div>
        <div className="flex">
          <div>
            <p className="font-breeSerif">Classe: </p>{processo.classe}
            <p className="font-breeSerif mt-2">Assunto: </p>{processo.assunto}
            <p className="font-breeSerif mt-2">Número do Processo: </p>{formatarProcesso(processo.numProcesso)}
          </div>
          <div className="ml-5">
            <p className="font-breeSerif">
              Nomes dos Autores:{" "}
            </p>
            {processo.nomeAutor.map((autor) => autor.nome).join()}
            <p className="font-breeSerif mt-2">
              Nomes dos Reus:{" "}
              
            </p>
            {processo.nomeReu.map((autor) => autor.nome).join()}
            <p className="font-breeSerif mt-2">Status: </p>{processo.status}
          </div>
        </div>
      </div>
    </div>
  );
}
