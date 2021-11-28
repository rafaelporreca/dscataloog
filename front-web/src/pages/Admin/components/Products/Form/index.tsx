import React, { useEffect, useState } from 'react';
import { makePrivateRequest, makeRequest } from 'core/Utils/request';
import { useForm, Controller } from 'react-hook-form';
import BaseForm from '../../BaseForm';
import { toast } from 'react-toastify';
import './styles.scss';
import { useHistory, useParams } from 'react-router';
import Select from 'react-select';
import { Category } from 'core/types/Product';


type FormState = {
    name: string;
    price: string;
    imgUrl: string;
    description: string;
    categories: Category[];
}

type ParamsType = {
    productId: string;
}

const Form = () => {
    const { register, handleSubmit, formState: { errors }, setValue, control } = useForm<FormState>();
    const history = useHistory();
    const { productId } = useParams<ParamsType>();
    const [isLoadingCategories, setIsLoadingCategories] = useState(false);
    const [categories, setCategories] = useState<Category[]>([]);
    const isEditing = productId !==  'create';
    const formTitle = isEditing ? "Editar produto" : "Cadastrar um produto"

    useEffect(() => {
        if (isEditing){
            makeRequest({url: `/products/${productId}`})
            .then(response => {
                setValue('name', response.data.name);
                setValue('price', response.data.price);
                setValue('imgUrl', response.data.imgUrl);
                setValue('description', response.data.description);
                setValue('categories', response.data.categories);
            })
        }
    },[productId, isEditing, setValue]);

    useEffect(() => {
        setIsLoadingCategories(true);
        makeRequest({ url: '/categories' })
        .then(response => setCategories(response.data.content))
        .finally(() => setIsLoadingCategories(false));
    },[]);

    const onSubmit = (data: FormState) => {
        makePrivateRequest({ 
            url: isEditing ? `/products/${productId}` : '/products', 
            method: isEditing ? 'PUT' : 'POST', 
            data 
        })
        .then(() => {
            toast.info('Produto salvo com sucesso!');
            history.push('/admin/products');
        })
        .catch(() => {
            toast.error('Erro ao salvar produto!');
        });
    }

    return (
        <form onSubmit={handleSubmit(onSubmit)}>
            <BaseForm 
            title={formTitle}
            >
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
                            <Controller
                                name="categories"
                                rules={{ required: true }}
                                control={control}
                                render={({ field: {onChange, value} }) => (
                                    <Select                                         
                                        options={categories}
                                        onChange={onChange}     
                                        value={value}                                   
                                        getOptionLabel={(option: Category) => option.name}
                                        getOptionValue={(option: Category) => String(option.id)}
                                        classNamePrefix="categories-select"
                                        placeholder="Categorias"
                                        isMulti 
                                        isLoading={isLoadingCategories}
                                     />
                                )}
                            />
                            {errors.categories && (
                                <div className="invalid-feedback d-block">
                                    Campo obrigatório
                                </div>
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
                                {...register('imgUrl', { required: "Campo obrigatório" })}
                                type="text"
                                name="imgUrl"
                                className="form-control input-base"
                                placeholder="URL da imagem"
                            />
                            {errors.imgUrl && (
                                <div className="invalid-feedback d-block">{errors.imgUrl.message}</div>
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