import ProductPrice from "core/components/ProductPrice";
import { Product } from "core/types/Product";
import React from "react";
import { Link } from "react-router-dom";
import './styles.scss';

type Props = {
    product: Product;
}

const Card = ({ product }: Props) => {

    return (
        <div className="card-base produtc-card-admin">
            <div className="row">
                <div className="col-2 text-center border-right py-3">
                    <img
                        src={product.imgUrl}
                        alt={product.name}
                        className="product-card-image-admin"
                    />
                </div>
                <div className="col-7 py-3">
                    <h3 className="product-card-name-admin">
                        {product.name}
                        <ProductPrice price={product.price} />
                        <div>
                            {product.categories.map(category => (
                                <span className="badge rounded-pill bg-secondary mr-2">
                                    categoria 1
                                </span>
                            ))}
                        </div>
                    </h3>
                </div>
                <div className="col-3 pt-3 pr-5">
                    <Link
                        to={`/admin/products/${product.id}`}
                        type="button"
                        className="btn btn-outline-secondary btn-block btn-edit border-radius-10 mb-3"
                    >
                        EDITAR
                    </Link>
                    <button
                        type="button"
                        className="btn btn-outline-danger btn-block border-radius-10"
                    >
                        EXCLUIR
                    </button>
                </div>
            </div>
        </div>
    )
}

export default Card;