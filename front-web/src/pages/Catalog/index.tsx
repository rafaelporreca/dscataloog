import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ProductsResponse } from '../../core/types/Product';
import { makeRequest } from '../../core/Utils/request';
import ProductCard from './components/ProductCard';
import ProductCardLoarder from './components/Loaders/ProductCardLoarder';
import './styles.scss';

const Catalog = () =>{

    const [productsResponse, setProductsResponse] = useState<ProductsResponse>();
    const [isLoading, setIsloading] = useState(false);

    useEffect(() => {
        const params ={
            page:0,
            linesPerPage:12
        }
        setIsloading(true);
        makeRequest({url: '/products', params})
        .then(response => setProductsResponse(response.data))
        .finally(() => {
            setIsloading(false)
        });
    },[]);

    return (
        <div className="catalog-container">
            <h1 className="catalog-title">Catálogo de produtos</h1>
            <div className="catalog-products">
                {isLoading ? <ProductCardLoarder/> : (
                    productsResponse?.content.map(product => (
                        <Link to={`/products/${product.id}`} key={product.id}><ProductCard product={product}/></Link>
                    ))
                )}
                             
            </div>
        </div>
    );
}

export default Catalog;