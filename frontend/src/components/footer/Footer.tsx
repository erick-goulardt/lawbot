import "../footer/Footer.style.css";
import Logo from "../../assets/teste-removebg-preview.png";

export function Footer() {
  return (
    <>
      <footer className="bg-sky-950 flex items-center">
        <div className="icon-footer flex font-breeSerif text-center justify-center items-center ml-28">
          <img src={Logo} className="w-12" alt="" />
          <h3 className="ml-5">Lawbot Company</h3>
        </div>
        <div className="term-footer flex font-breeSerif mr-32">
          <p className="mr-7 cursor-pointer">Sobre</p>
          <p className="mr-7 cursor-pointer">Termos</p>
          <p className="mr-7 cursor-pointer">Privacidade</p>
        </div>
      </footer>
    </>
  );
}
