export interface IProfile {
  id: number;
  nome: string;
  email: string;
  oab: string;
  cpf: string;
  dataNascimento: string;
}

export interface IDefineCliente {
  id: number | undefined;
  processo: IProcesso | null;
}

export interface IEditProfile {
  nome: string;
  email: string;
  senha: string;
}

export interface IDefineDescricao {
  descricao: string;
  processo: IProcesso | null;
}

export interface IEditCliente {
  nome: string;
  email: string;
  cpf: string;
}

export interface Reu {
  nome: string;
}

export interface Autor {
  nome: string;
}

export interface IProcesso {
  id: number;
  numProcesso: string;
  status: string;
  classe: string;
  assunto: string;
  dataAtualizacao: string;
  descricao: string;
  nomeReu: Reu[];
  nomeAutor: Autor[];
}


export interface Cliente {
    id: number;
    nome: string;
    email: string;
    cpf: string;
  }

  export interface IProcessoHistorico {
    atualizacao: string;
    dataAtualizacao: string;
    descricao: string;
  }

  export interface Nome {
    nome: string;
  }
  
  export interface IidAdvogado {
    id: number;
  }
  
  export interface IidCliente {
    id: number;
  }
  
  export interface ICadastraProcesso {
    advogado: IidAdvogado;
    cliente: IidCliente;
    ultimoEvento: string;
    dataAtualizacao: string;
    descricao: string;
    numeroProcesso: string;
    classe: string;
    localidade: string;
    assunto: string;
    nomeReu: Reu;
    nomeAutor: Autor;
  }
  