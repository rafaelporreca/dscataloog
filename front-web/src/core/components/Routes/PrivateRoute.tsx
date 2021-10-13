import React from "react";
import {Redirect, Route} from "react-router-dom";
import {isAllowedByRole, isAutenticated, Role} from "../../Utils/auth";

type Props = {
    children: React.ReactNode;
    path: string;
    allowedRoutes?: Role[];
}

const PrivateRoute = ({children, path, allowedRoutes}: Props) => {
    return (
        <Route
            path={path}
            render={props => {
                if (!isAutenticated()) {
                    return (
                        <Redirect
                            to={{
                                pathname: "/auth/login",
                                state: {from: props.location}
                            }}
                        />
                    )
                } else if(isAutenticated() && !isAllowedByRole(allowedRoutes)) {
                    return (
                        <Redirect to={{ pathname: "/admin" }} />
                    )
                }
                return children
            }}
        />
    );
}

export default PrivateRoute;