import { ChangeEvent } from "react";

interface InputProps {
  label: string;
  value: string;
  onChange: (e: ChangeEvent<HTMLInputElement>) => void;
  placeholder: string;
  className: string;
  type: string;
  name: string;
}

export function Input({
  label,
  value,
  name,
  onChange,
  placeholder,
  className,
  type,
}: InputProps) {
  return (
    <>     
      <div className=" flex flex-col text-start mt-4">
      <label className="font-bold pb-2 font-breeSerif">{label}</label>
        <input
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
          className={className}
          name={name}
        />
      </div>
    </>
  );
}
