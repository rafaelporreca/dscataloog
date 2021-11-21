import React, { useState } from 'react';
import { makePrivateRequest } from 'core/Utils/request';
import { useForm } from 'react-hook-form';
import BaseForm from '../../BaseForm';
import './styles.scss';

type FormState = {
    name: string;
    price: string;
    imageUrl: string;
    description: string;
}

const Form = () => {
    const { register, handleSubmit, formState: { errors } } = useForm<FormState>();

    const onSubmit = (data: FormState) => {
        makePrivateRequest({ url: '/products', method: 'POST', data });
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <BaseForm title="Cadastrar um produto">
                <div className="row">
                    <div className="col-6">
                        <div className="margin-bottom-30">
                            <input
                                type="text"
                                {...register('name', {
                                    required: "Campo obrigatório",
                                    minLength: { value: 5, message: "O campo deve ter no mínimo 5 caracteres" },
                                    maxLength: { value: 60, message: "O campo deve ter no máximo 60 caracteres" }
                                })}
                                name="name"
                                className="form-control input-base"
                                placeholder="Nome do Produto"
                            />
                            {errors.name && (
                                <div className="invalid-feedback d-block">{errors.name.message}</div>
                             )}
                        </div>
                        <div className="margin-bottom-30">
                            <input
                                {...register('price', { required: "Campo obrigatório" })}
                                type="number"
                                name="price"
                                className="form-control input-base"
                                placeholder="Preço"
                            />
                            {errors.price && (
                                <div className="invalid-feedback d-block">{errors.price.message}</div>
                            )}
                        </div>
                        <div className="margin-bottom-30">
                            <input
                                {...register('imageUrl', { required: "Campo obrigatório" })}
                                type="text"
                                name="imageUrl"
                                className="form-control input-base"
                                placeholder="Nome do Produto"
                            />
                            {errors.imageUrl && (
                                <div className="invalid-feedback d-block">{errors.imageUrl.message}</div>
                            )}
                        </div>
                    </div>
                    <div className="col-6">
                        <textarea
                            {...register('description', { required: "Campo obrigatório" })}
                            className="form-control input-base"
                            placeholder="Descrição"
                            name="description"
                            cols={30}
                            rows={10}
                        />
                        {errors.description && (
                                <div className="invalid-feedback d-block">{errors.description.message}</div>
                        )}
                    </div>
                </div>
            </BaseForm>
        </form>
    );
}

export default Form;