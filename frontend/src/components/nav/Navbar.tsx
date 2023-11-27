import { NavLink } from "react-router-dom";
import '../../components/nav/Navbar.style.css'
import  Icon  from "../../assets/logo.svg"

type NavbarProps = {
  links: {
    to: string;
    label: string;
    onClick?: () => void;
    dropdown?: React.ReactNode;
  }[];
};

export function Navbar({ links }: NavbarProps) {
  return (
    <nav className="navbar">
      <div className="logo">
        <img src={Icon} alt="" className="w-16" />
        <h4 className="font-breeSerif text-3xl">Lawbot</h4>
      </div>
      <div className="nav-elements">
        <ul>
          {links.map((link, index) => (
            <li key={index}>
              {link.dropdown ? (
                <div className="dropdown-container">
                  <span
                    className="font-breeSerif cursor-pointer"
                    onClick={link.onClick}
                  >
                    {link.label}
                  </span>
                  {link.dropdown}
                </div>
              ) : (
                <NavLink
                  to={link.to}
                  className="font-breeSerif"
                  onClick={link.onClick}
                >
                  {link.label}
                </NavLink>
              )}
            </li>
          ))}
        </ul>
      </div>
    </nav>
  );
}
