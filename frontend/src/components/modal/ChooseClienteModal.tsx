import { useState, useEffect } from "react";
import Select from "react-select";
import { getClientesFromAdvogado } from "../../api/advogado.service";
import { Cliente } from "../../types/Types";

interface CustomModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSave: (data: ModalData) => void;
  idAdvogado: number | undefined;
}

interface ModalData {
  selectedOption: { label: string; value: number } | null;
}

export function CustomModal({isOpen, onClose, onSave, idAdvogado}: CustomModalProps) {
  const [selectedOption, setSelectedOption] = useState<ModalData["selectedOption"]>(null);
  const [clientesOptions, setClientesOptions] = useState<{ label: string; value: number }[] | null>(null);

  const showClientes = async () => {
    try {
      const response = await getClientesFromAdvogado(idAdvogado);
      if (response && Array.isArray(response.data)) {
        const formattedOptions = response.data.map((cliente: Cliente) => ({
          label: cliente.nome,
          value: cliente.id,
        }));
        setClientesOptions(formattedOptions);
      } else {
        console.error("Invalid data format received from server");
      }
    } catch (error) {
      console.error("Erro ao buscar cliente", error);
    }
  };

  useEffect(() => {
    if (isOpen) {
      showClientes();
    }
  }, [isOpen]);

  const handleSave = () => {
    onSave({
      selectedOption
    });

    onClose();
  };

  const handleModalClose = () => {
    onClose();
  };

  return (
    <div
      style={{
        display: isOpen ? "block" : "none",
        position: "fixed",
        top: 0,
        left: 0,
        width: "100%",
        height: "100%",
        background: "rgba(0,0,0,0.5)",
      }}
    >
      <div
        style={{
          position: "absolute",
          top: "50%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          background: "#fff",
          padding: "20px",
          borderRadius: "8px",
          maxWidth: "400px",
          width: "25rem"
        }}
      > <button onClick={handleModalClose}>X</button>
        <h2 className="text-center font-breeSerif text-xl pt-3">
          Vincular Cliente ao Processo
        </h2>
        <div className="flex flex-col p-5">
          <div>
            <label className="font-boldInter text-base text-black">Selecione uma opção:</label>
            <Select
            className="mb-5 mt-3"
              value={selectedOption}
              onChange={(value) => setSelectedOption(value)}
              options={clientesOptions || []}
              
            />
          </div>
          <button className="bg-slate-400 text-white rounded-lg pt-2 pb-2" onClick={handleSave}>Salvar</button>
        </div>
      </div>
    </div>
  );
}

export default CustomModal;
