import React, { useCallback, useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { ProductsResponse } from '../../core/types/Product';
import { makeRequest } from '../../core/Utils/request';
import ProductCard from './components/ProductCard';
import ProductCardLoarder from './components/Loaders/ProductCardLoarder';
import './styles.scss';
import Pagination from 'core/components/Pagination';
import ProductFilters, { FilterForm } from 'core/components/ProductFilters';

const Catalog = () =>{

    const [productsResponse, setProductsResponse] = useState<ProductsResponse>();
    const [isLoading, setIsloading] = useState(false);
    const [activePage, setActivePage] = useState(0);

    const getProducts = useCallback((filter?: FilterForm) => {
        const params ={
            page:activePage,
            linesPerPage:12,
            name: filter?.name,
            categoryId: filter?.categoryId
        }
        setIsloading(true);
        makeRequest({url: '/products', params})
        .then(response => setProductsResponse(response.data))
        .finally(() => {
            setIsloading(false)
        });
    },[activePage]);

    useEffect(() => {
        getProducts()
    },[getProducts]);

    return (
        <div className="catalog-container">
            <div className="d-flex justify-content-between">
                <h1 className="catalog-title">Catálogo de produtos</h1>
                <ProductFilters onSearch={filter => getProducts(filter)}/>
            </div>
            <div className="catalog-products">
                {isLoading ? <ProductCardLoarder/> : (
                    productsResponse?.content.map(product => (
                        <Link to={`/products/${product.id}`} key={product.id}><ProductCard product={product}/></Link>
                    ))
                )}
                             
            </div>
            {productsResponse && <Pagination totalPages={productsResponse.totalPages} activePage={activePage} onChange={page=> setActivePage(page)}/>}
        </div>
    );
}

export default Catalog;