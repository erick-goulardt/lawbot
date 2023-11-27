import { ReactNode } from "react";

type ButtonProps = {
    type?: "button" | "submit" | "reset";
    onClick?: (event: MouseEvent) => void,
    children: ReactNode;
    className: string;
    
}

export function Button({type, children, className} : ButtonProps) {
    return (
        <>
            <button className={className} type={type}>{children}</button>
        </>
    )   
}