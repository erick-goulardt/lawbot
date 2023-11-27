import { Navbar } from "../components/nav/Navbar";
import LawIcon from "../assets/law.svg"
import { Footer } from "../components/footer/Footer";

export function HomePage() {
  return (
    <>
      <Navbar 
            links={[
              { to: '/', label: 'Início' },
              { to: '/login', label: 'Entrar' },
              { to: '/registrar', label: 'Registrar' },
            ]}
      />
      <main className="w-full flex">
        <section className="container-desc w-3/6 flex flex-col ml-36 mt-6">
          <h1 className="motto font-breeSerif text-7xl w-96 mt-12">
            O ESPÍRITO DO NEGÓCIO
          </h1>
          <p className="font-lightInter text-lg w-5/12 mt-14">
            Tempo é dinheiro, e dinheiro é tempo. Sabendo disso, nossa empresa
            desenvolveu uma plataforma capaz de auxiliar e otimizar todo tempo e
            tarefas rotineiras dentro de seu escritório jurídico voltado para a
            parte trabalhista.
          </p>
          <div className="flex mt-14">
            <div className="w-28 mr-4">
              <h3 className="font-breeSerif">Otimização</h3>
              <p>Valorizamos seu tempo útil.</p>
            </div>
            <div className="w-28 mr-4">
              <h3 className="font-breeSerif">Intuitivo</h3>
              <p>Fácil e prático de ser usado.</p>
            </div>
            <div className="w-40 mr-4">
              <h3 className="font-breeSerif">Facilitador</h3>
              <p>Muitas de suas tarefas de forma facilitada.</p>
            </div>
          </div>
        </section>
        <section className="w-4/5 flex justify-end">
            <img src={LawIcon} className="w-2/5 mr-36 mb-12" alt="" />
        </section>
      </main>
      <Footer />
    </>
  );
}
