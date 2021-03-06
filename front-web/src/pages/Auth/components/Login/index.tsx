import React, {useState} from "react";
import './styles.scss'
import AuthCard from "../Card";
import {Link, useHistory} from "react-router-dom";
import { useForm } from 'react-hook-form';
import Buttonicon from "../../../../core/components/Buttonicon";
import {makeLogin} from "../../../../core/Utils/request";
import {saveSessionData} from "../../../../core/Utils/auth";

type FormData = {
    username: string;
    password: string;
}

const Login = () => {
    const {register, handleSubmit, formState: { errors }} = useForm<FormData>();
    const [hasError, setHasError] = useState(false);
    const history = useHistory();
    const onSubmit = (data: FormData) => {
        makeLogin(data)
            .then(response => {
                setHasError(false);
                saveSessionData(response.data);
                history.push('/admin')
            })
            .catch(() => {
                setHasError(true);
            })
    }


    return (
        <AuthCard title="login">
            {hasError && (<div className="alert alert-danger mt-5">
                Usuário ou senha inválidos!
            </div>)}
            <form className="login-form" onSubmit={handleSubmit(onSubmit)}>
                <div className="margin-bottom-30">
                    <input {...register('username', {required: "Campo obrigatório", pattern: {
                            value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                            message: "Email inválido"
                        }})} type="email" className={`form-control input-base ${errors.username ? 'is-invalid' : ''} `}
                           placeholder="Email"/>
                    {errors.username && (
                        <div className="invalid-feedback d-block">{errors.username.message}</div>
                    )}

                </div>
                <div className="margin-bottom-30">
                    <input {...register('password', {required: "Campo obrigatório"})} type="password"
                           className={`form-control input-base ${errors.password ? 'is-invalid' : ''} `}
                           placeholder="Senha"/>
                    <Link to="/admin/auth/recover" className="login-link-recover">
                        Esqueci a senha?
                    </Link>
                    {errors.password && (
                        <div className="invalid-feedback d-block">{errors.password.message}</div>
                    )}
                </div>
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