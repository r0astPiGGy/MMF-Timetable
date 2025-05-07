import { Pool, PoolClient } from "https://deno.land/x/postgres@v0.17.0/mod.ts";

// @deno-types="npm:@types/express@^4.17"
import express, { Request } from "npm:express@4.18.2";
// @deno-types="npm:@types/jsonwebtoken@9.0.2"
import jwt from "npm:jsonwebtoken@9.0.2";
import cors from "npm:cors@2.8.5";

import teacher from "./controllers/teacher.ts";
import classroom from "./controllers/classroom.ts";
import course from "./controllers/course.ts";
import lesson from "./controllers/lesson.ts";
import subgroupSubject from "./controllers/subgroup-subject.ts";

export type QueryParams = {
    group?: string;
    id?: number;
};

const pool = new Pool(
    {
        database: "postgres",
        hostname: Deno.env.get("DB_HOSTNAME"),
        password: Deno.env.get("DB_PASSWORD"),
        port: Deno.env.get("DB_PORT"),
        user: Deno.env.get("DB_USER"),
        tls: { enabled: false },
        host_type: "tcp",
    },
    1,
    true,
);

export const useConnection = async (fn: (connection: PoolClient) => void) => {
    try {
        const connection = await pool.connect();
        try {
            fn(connection);
        } catch (error) {
            console.log(error);
        } finally {
            connection.release();
            console.log("Released connection");
        }
    } catch (error) {
        console.log(error);
    }
};

export const app = express();
app.use(express.json());
const port = 3000;

app.use((req, res, next) => {
    const authHeader = req.headers["authorization"];
    const token = authHeader && authHeader.split(" ")[1];

    if (token == null) return res.sendStatus(401);

    jwt.verify(token, Deno.env.get("JWT_VERIFY") as string, (err, auth) => {
        if (err) return res.status(403).send(err);

        next();
    });
});

app.use(cors());

app.param(
    ["id"],
    function (req: Request<any, any, {}>, res, next, num, name) {
        req.params[name] = parseInt(num, 10);

        if (isNaN(req.params[name])) {
            res.status(400).send(
                "Invalid format for " + name + ", should be an integer",
            );
        } else {
            next();
        }
    },
);

app.use("/timetable-api/teacher", teacher);
app.use("/timetable-api/classroom", classroom);
app.use("/timetable-api/course", course);
app.use("/timetable-api/lesson", lesson);
app.use("/timetable-api/subgroup-subject", subgroupSubject);

app.listen(port, () => {
    console.log(`Example app listening on port ${port}`);
});
