import React from "react";
import './styles.scss'
import AuthCard from "../Card";
import { Link } from "react-router-dom";
import { useForm } from 'react-hook-form';
import Buttonicon from "../../../../core/components/Buttonicon";

type FormData = {
    email: string;
    password: string;
}

const Login = () => {
    const { register, handleSubmit } = useForm<FormData>();
    const onSubmit = (data: FormData) => {
        console.log(data);
    }
    return (
        <AuthCard title="login">
            <form className="login-form"  onSubmit={handleSubmit(onSubmit)}>
                <input {...register('email')} type="email" className="form-control input-base margin-bottom-30" placeholder="Email"/>
                <input {...register('password')} type="password" className="form-control input-base" placeholder="Senha"/>
                <Link to="/admin/auth/recover" className="login-link-recover">
                    Esqueci a senha?
                </Link>
                <div className="login-submit">
                    <Buttonicon text="logar"/>
                </div>
                <div className="text-center">
                    <span className="not-registred">Não tem cadastro?</span>
                    <Link to="/admin/auth/cadastrar" className="login-link-register">CADASTRAR</Link>
                </div>
            </form>
        </AuthCard>
    )
}

export default Login;