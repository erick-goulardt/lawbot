import { createContext, useContext, useState, useCallback, ReactNode } from 'react';

interface AuthContextType {
  user: User | null;
  login: (user: User) => void;
  logout: () => void;
  isAuthenticated: () => boolean;
}

interface User {
  id: number;
  token: string;
}

export const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const storageUser = localStorage.getItem("user");
  const defaultUser = storageUser ? JSON.parse(storageUser) : null;
  const [user, setUser] = useState<User | null>(defaultUser);

  const login = useCallback((newUser: User) => {
    setUser(newUser);
  }, []);


  const logout = useCallback(() => {
    localStorage.removeItem("user")
    setUser(null);
  }, []);

  const isAuthenticated = useCallback(() => {
    return !!user;
  }, [user]);


  return (
    <AuthContext.Provider value={{ user, login, logout, isAuthenticated }}>
      {children}
    </AuthContext.Provider>
  );
};

// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};



