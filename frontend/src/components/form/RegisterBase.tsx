import { FormEvent, ReactNode } from "react";
import Image from "../../assets/teste-removebg-preview.png";
import { useNavigate } from "react-router-dom";

type FormProps = {
  onSubmit: (e: FormEvent<HTMLFormElement>) => void;
  children: ReactNode;
};

export function RegisterBase({ onSubmit, children }: FormProps) {
  const navigate = useNavigate();

  const returnBack = () => {
    navigate("/");
  };

  return (
    <div className="flex w-3/4 h-full justify-center items-center rounded-lg">
      <div className="w-4/6 h-5/6 bg-blue-900 h-720 flex flex-col rounded-l-lg justify-center">
        <img
          width="48"
          height="48"
          src="https://img.icons8.com/fluency/48/back.png"
          alt="back"
          className="ml-16 mb-10 cursor-pointer"
          onClick={returnBack}
        />
        <div className="pl-8 ml-12">
          <img src={Image} className="-ml-6 -mt-8 pb-6" alt="" />
          <h2 className="text-white font-breeSerif text-5xl pr-40">
            Venha fazer parte do software mais sofisticado para comunicar com
            seus clientes
          </h2>
        </div>
      </div>
      <form
        action=""
        onSubmit={onSubmit}
        className="w-1/3 h-5/6 bg-white justify-center flex flex-col align-middle text-center rounded-r-lg items-center"
      >
        {children}
      </form>
    </div>
  );
}
