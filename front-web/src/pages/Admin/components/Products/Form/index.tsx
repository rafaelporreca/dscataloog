import {makePrivateRequest} from 'core/Utils/request';
import React, { useState } from 'react';
import BaseForm from '../../BaseForm';
import './styles.scss';

type FormState = {
    name: string;
    price: string;
    category: string;
    description: string;
}

type FormEvent = React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>;

const Form = () => {

    const [formData,setFormData] = useState<FormState>({
        name: '',
        price: '',
        category: '',
        description: ''
    });

    const handleOnChange = (event: FormEvent) =>{
        const name = event.target.name;
        const value = event.target.value;

        setFormData(data => ({ ...data,[name]:value }));
    }

    const handleSubimit = (event: React.FormEvent<HTMLFormElement>) =>{
        event.preventDefault();
        const payload = {
            ...formData,
            imgUrl: 'https://multiversomais.com/wp-content/uploads/2020/08/XboxSeriesXTech_Inline1.jpg',
            categories: [{ id: formData.category}]
        }

        makePrivateRequest({url: '/products', method:'POST', data: payload}).then(() => {
            setFormData({name: '', category: '', price: '', description: ''});
        });
    }

    return(
        <form onSubmit={handleSubimit}>
            <BaseForm title="Cadastrar um produto">
                <div className="row">
                    <div className="col-6">
                        <input type="text" value={formData.name} name="name" className="form-control mb-5" onChange={handleOnChange} placeholder="Nome do Produto"/>
                        <select value={formData.category} name="category" className="form-control mb-5" onChange={handleOnChange}>
                        <option value="1">Livros</option>
                        <option value="3">Computadores</option>
                        <option value="2">Eletrônicos</option>
                        </select>
                        <input type="text" value={formData.price} name="price" className="form-control mb-5" onChange={handleOnChange} placeholder="Preço"/>                
                    </div>
                    <div className="col-6">
                        <textarea className="form-control" value={formData.description} name="description" onChange={handleOnChange} cols={30} rows={10}/>
                    </div>
                </div>
            </BaseForm>
        </form>
    );
}

export default Form;